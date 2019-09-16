package com.bootvue.common.config.web;

import com.bootvue.common.type.Result;
import com.bootvue.common.type.ResultCode;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * 处理全局返回
 */
public class GlobalResponseHandle implements HandlerMethodReturnValueHandler {
    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    public GlobalResponseHandle(RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor) {
        this.requestResponseBodyMethodProcessor = requestResponseBodyMethodProcessor;
    }

    public boolean supportsReturnType(MethodParameter returnType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) || returnType.hasMethodAnnotation(ResponseBody.class);
    }

    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        Result response = null;
        if (!Result.class.isAssignableFrom(returnType.getParameterType())) {
            response = new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), returnValue);
        } else {
            response = (Result) returnValue;
        }

        this.requestResponseBodyMethodProcessor.handleReturnValue(response, returnType, mavContainer, webRequest);
    }
}
