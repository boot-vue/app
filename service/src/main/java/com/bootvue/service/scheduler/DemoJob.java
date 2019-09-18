package com.bootvue.service.scheduler;

import com.bootvue.autoconfigure.ElasticJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ElasticJob(jobName = "demoJob", cron = "0/3 * * * * ?", shardCount = 1, overwrite = true, stream = false)
public class DemoJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("demoJob正在执行, 参数:{}", shardingContext);
    }
}
