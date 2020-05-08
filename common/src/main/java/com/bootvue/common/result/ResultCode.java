package com.bootvue.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码
 */
@AllArgsConstructor
@Getter
public enum ResultCode {
    DEFAULT(600, "系统异常"),
    SUCCESS(200, "success"),
    TOKEN_ERROR(401, "凭证无效"),
    ACCESS_DENY(403, "无权访问"),
    LOGIN_ERROR(601, "用户名或密码错误"),
    PARAM_ERROR(602, "参数错误");
    private final Integer code;
    private final String msg;
}
