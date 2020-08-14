package com.bootvue.service.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoJob implements SimpleJob {

    @Override
    public void execute(final ShardingContext shardingContext) {
        log.info("shardingContext: {}", shardingContext.toString());

    }
}
