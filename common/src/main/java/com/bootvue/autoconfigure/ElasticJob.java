package com.bootvue.autoconfigure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * elastic 任务注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJob {
    String jobName() default "";//作业任务名  全局唯一,不要重复

    String cron() default "";//cron表达式

    int shardCount() default 1;//总分片数, 如果不能处理好分布式场景下的数据处理, 务必传1

    boolean overwrite() default true;//每次重启是否复写zookeeper中任务调度的配置

    boolean stream() default false;//是否开启dataflow
}
