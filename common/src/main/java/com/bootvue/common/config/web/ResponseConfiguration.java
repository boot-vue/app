package com.bootvue.common.config.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ResponseConfiguration implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        final List<HandlerMethodReturnValueHandler> originalHandlers = new ArrayList<>(requestMappingHandlerAdapter.getReturnValueHandlers());

        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;
        for (int i = 0; i < originalHandlers.size(); i++) {
            final HandlerMethodReturnValueHandler valueHandler = originalHandlers.get(i);
            if (RequestResponseBodyMethodProcessor.class.isAssignableFrom(valueHandler.getClass())) {
                requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) valueHandler;
                break;
            }
        }
        ResponseObjectHandler aronResponseValueHandler = new ResponseObjectHandler(requestResponseBodyMethodProcessor);

        final int deferredPos = obtainValueHandlerPosition(originalHandlers, DeferredResultMethodReturnValueHandler.class);
        originalHandlers.add(deferredPos + 1, aronResponseValueHandler);
        requestMappingHandlerAdapter.setReturnValueHandlers(originalHandlers);
    }

    private int obtainValueHandlerPosition(final List<HandlerMethodReturnValueHandler> originalHandlers, Class<?> handlerClass) {
        for (int i = 0; i < originalHandlers.size(); i++) {
            final HandlerMethodReturnValueHandler valueHandler = originalHandlers.get(i);
            if (handlerClass.isAssignableFrom(valueHandler.getClass())) {
                return i;
            }
        }
        return -1;
    }
}
