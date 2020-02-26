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
    LOGIN_ERROR(601, "用户名或密码错误"),
    TOKEN_ERROR(401, "凭证无效"),
    ACCESS_DENY(403, "无权访问");
    private Integer code;
    private String msg;
}
