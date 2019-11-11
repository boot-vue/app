package com.bootvue.auth.authc;

import com.bootvue.auth.util.JsonUtil;
import com.bootvue.common.result.ResultCode;
import com.bootvue.common.result.ResultUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        JsonUtil.write(response, ResultUtil.error(ResultCode.LOGIN_ERROR));
    }
}
