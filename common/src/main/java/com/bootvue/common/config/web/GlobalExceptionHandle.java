package com.bootvue.common.config.web;

import com.bootvue.common.result.AppException;
import com.bootvue.common.result.R;
import com.bootvue.common.result.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler(value = AppException.class)
    @ResponseBody
    public <T> R<T> handleException(AppException e) {
        return new R<>(e.getCode(), e.getMsg(), null);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseBody
    public <T> R<T> handleException(Exception e) {
        log.error("拦截到异常: ", e);
        return new R<T>(RCode.DEFAULT.getCode(), StringUtils.isEmpty(e.getMessage()) ? RCode.DEFAULT.getMsg() : e.getMessage(), null);
    }

}
