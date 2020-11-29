package com.bootvue.common.result;

import org.springframework.validation.BindingResult;

import java.util.Objects;

public class ResultUtil {
    public static <T> R<T> success(T data) {
        return new R<T>(RCode.SUCCESS.getCode(), RCode.SUCCESS.getMsg(), data);
    }

    public static <T> R<T> success() {
        return success(null);
    }

    public static <T> R<T> error(RCode code) {
        return new R<T>(code.getCode(), code.getMsg(), null);
    }

    public static void handleErr(BindingResult result) {
        if (result.hasErrors()) {
            String msg = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            throw new AppException(RCode.PARAM_ERROR);
        }
    }
}
