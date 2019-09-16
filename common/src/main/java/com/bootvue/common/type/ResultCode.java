package com.bootvue.common.type;

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
    AUTHENTICATION_ERROR(602, "用户未登录"),
    AUTHORIZATION_ERROR(603, "没有权限");
    private Integer code;
    private String msg;
}
