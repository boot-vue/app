package com.bootvue.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * elastic-job 属性配置
 */
@Configuration
@ConfigurationProperties(prefix = "scheduler.config")
@Getter
@Setter
public class ElasticJobProperties {
    private String entrypoint;//zookeeper 地址:端口,  ,隔开多个配置 如: host1:2181,host2:2181
    private String namespace; //命名空间
}
