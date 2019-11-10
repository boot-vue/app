package com.bootvue.auth.authc;

import com.alibaba.fastjson.JSON;
import com.bootvue.common.result.ResultUtil;
import io.netty.util.CharsetUtil;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回token
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(CharsetUtil.UTF_8.toString());

        String token = UUID.randomUUID().toString();
        response.getWriter().write(JSON.toJSONString(ResultUtil.success(token)));
    }
}
