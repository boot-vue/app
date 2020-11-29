package com.bootvue.auth.handler;

import com.bootvue.auth.util.ResponseUtil;
import com.bootvue.common.result.R;
import com.bootvue.common.result.RCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        ResponseUtil.write(response, new R<String>(RCode.UNAUTHORIZED_ERROR.getCode(), e.getMessage(), null));
    }
}
