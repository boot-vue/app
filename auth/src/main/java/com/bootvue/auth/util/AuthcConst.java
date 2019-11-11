package com.bootvue.auth.util;

import java.time.Duration;

/**
 * 常量配置
 */
public class AuthcConst {

    public static final String LOGIN_URL = "/login";
    public static final String SMS_LOGIN_URL = "/login/sms";
    public static final String LOGOUT_URL = "/logout";

    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";

    public static final String PHONE_KEY = "phone";
    public static final String PHONE_CODE = "code";

    public static final Long TOKEN_EXPIRE = Duration.ofDays(30L).toMillis();// token有效期

}
