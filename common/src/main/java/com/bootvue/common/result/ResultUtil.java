package com.bootvue.common.result;

public class ResultUtil {
    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(ResultCode code) {
        return new Result(code.getCode(), code.getMsg(), null);
    }
}
