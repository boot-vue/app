package com.bootvue.autoconfigure;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * elastic-job 自动配置
 */
@Configuration
@ConditionalOnProperty("scheduler.config.entrypoint")
@EnableConfigurationProperties(ElasticJobProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticJobAutoConfiguration {

    private final ElasticJobProperties elasticJobProperties;
    private final ApplicationContext applicationContext;
    private final DataSource dataSource;

    @Bean
    public CoordinatorRegistryCenter coordinatorRegistryCenter() {
        //初始化 zookeeper连接
        ZookeeperConfiguration configuration = new ZookeeperConfiguration(elasticJobProperties.getEntrypoint(), elasticJobProperties.getNamespace());
        ZookeeperRegistryCenter registryCenter = new ZookeeperRegistryCenter(configuration);
        registryCenter.init();

        //反射  扫描注入 所有@ElasticJob 注解的任务对象
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ElasticJob.class);
        if (CollectionUtils.isEmpty(beans)) {
            return registryCenter;
        }

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object object = entry.getValue();
            //所有已实现的接口
            Class<?>[] interfaces = object.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                ElasticJob elasticJob = object.getClass().getAnnotation(ElasticJob.class);
                String jobName = elasticJob.jobName();
                String cron = elasticJob.cron();
                int shardCount = elasticJob.shardCount();
                boolean overwrite = elasticJob.overwrite();
                boolean stream = elasticJob.stream();

                if (anInterface == SimpleJob.class) { //simple-job 加载
                    JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobName, cron, shardCount).build();

                    SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, object.getClass().getCanonicalName());

                    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(overwrite).build();

                    JobEventConfiguration jobEventConfiguration = new JobEventRdbConfiguration(dataSource);

                    new SpringJobScheduler((com.dangdang.ddframe.job.api.ElasticJob) object, registryCenter, liteJobConfiguration, jobEventConfiguration).init();
                } else if (anInterface == DataflowJob.class) {//data flow任务加载
                    JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobName, cron, shardCount).build();

                    DataflowJobConfiguration jobConfiguration = new DataflowJobConfiguration(jobCoreConfiguration, object.getClass().getCanonicalName(), stream);

                    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(jobConfiguration).overwrite(overwrite).build();

                    JobEventConfiguration jobEventConfiguration = new JobEventRdbConfiguration(dataSource);

                    new SpringJobScheduler((com.dangdang.ddframe.job.api.ElasticJob) object, registryCenter, liteJobConfiguration, jobEventConfiguration).init();
                }
            }
        }

        return registryCenter;
    }

}
