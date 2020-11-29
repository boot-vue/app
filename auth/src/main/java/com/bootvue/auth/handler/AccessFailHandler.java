package com.bootvue.auth.handler;

import com.bootvue.auth.util.ResponseUtil;
import com.bootvue.common.result.RCode;
import com.bootvue.common.result.ResultUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 重写403响应
 */
@Component
public class AccessFailHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ResponseUtil.write(response, ResultUtil.error(RCode.ACCESS_DENY));
    }
}
