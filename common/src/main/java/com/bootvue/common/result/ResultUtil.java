package com.bootvue.common.result;

import org.springframework.validation.BindingResult;

import java.util.Objects;

public class ResultUtil {
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(ResultCode code) {
        return new Result<T>(code.getCode(), code.getMsg(), null);
    }

    public static void handleErr(BindingResult result) {
        if (result.hasErrors()) {
            String msg = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            throw new AppException(ResultCode.PARAM_ERROR);
        }
    }
}
