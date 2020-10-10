package com.bootvue.common.config.web;

import com.bootvue.common.result.Result;
import com.bootvue.common.result.ResultUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

public class ResponseObjectHandler implements HandlerMethodReturnValueHandler {

    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    public ResponseObjectHandler(RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor) {
        this.requestResponseBodyMethodProcessor = requestResponseBodyMethodProcessor;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) || returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        Result result = null;
        if (!Result.class.isAssignableFrom(returnType.getParameterType())) {
            result = ResultUtil.success(returnValue);
        } else {
            result = (Result) returnValue;
        }
        this.requestResponseBodyMethodProcessor.handleReturnValue(result, returnType, mavContainer, webRequest);
    }
}
