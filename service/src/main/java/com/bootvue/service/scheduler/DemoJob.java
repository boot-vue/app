package com.bootvue.service.scheduler;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class DemoJob {

    @XxlJob("testJob")
    public ReturnT<String> test(String param) {
        log.info("参数: {}", param);
        return new ReturnT<>(200, String.valueOf(System.currentTimeMillis()));
    }
}
