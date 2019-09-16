package com.bootvue.common.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void initResponseValue() {
        List<HandlerMethodReturnValueHandler> originalHandlers = new ArrayList(this.requestMappingHandlerAdapter.getReturnValueHandlers());
        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;

        for (int i = 0; i < originalHandlers.size(); ++i) {
            HandlerMethodReturnValueHandler valueHandler = originalHandlers.get(i);
            if (RequestResponseBodyMethodProcessor.class.isAssignableFrom(valueHandler.getClass())) {
                requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) valueHandler;
                break;
            }
        }

        GlobalResponseHandle aronResponseValueHandler = new GlobalResponseHandle(requestResponseBodyMethodProcessor);
        int deferredPos = this.obtainValueHandlerPosition(originalHandlers, DeferredResultMethodReturnValueHandler.class);
        originalHandlers.add(deferredPos + 1, aronResponseValueHandler);
        this.requestMappingHandlerAdapter.setReturnValueHandlers(originalHandlers);
    }

    private int obtainValueHandlerPosition(final List<HandlerMethodReturnValueHandler> originalHandlers, Class<?> handlerClass) {
        for (int i = 0; i < originalHandlers.size(); ++i) {
            HandlerMethodReturnValueHandler valueHandler = originalHandlers.get(i);
            if (handlerClass.isAssignableFrom(valueHandler.getClass())) {
                return i;
            }
        }

        return -1;
    }
}
