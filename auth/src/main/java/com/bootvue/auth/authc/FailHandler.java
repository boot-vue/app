package com.bootvue.auth.authc;

import com.alibaba.fastjson.JSON;
import com.bootvue.common.result.ResultCode;
import com.bootvue.common.result.ResultUtil;
import io.netty.util.CharsetUtil;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(CharsetUtil.UTF_8.toString());

        response.getWriter().write(JSON.toJSONString(ResultUtil.error(ResultCode.LOGIN_ERROR)));
    }
}
