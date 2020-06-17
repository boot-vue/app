package com.bootvue.common.result;

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
}
