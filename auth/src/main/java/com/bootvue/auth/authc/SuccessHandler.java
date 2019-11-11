package com.bootvue.auth.authc;

import com.bootvue.auth.util.AuthcConst;
import com.bootvue.auth.util.ResponseUtil;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回token
        Map<String, Object> params = new HashMap<>();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        params.put("username", userDetails.getUsername());
        params.put("authorities", userDetails.getAuthorities());

        // jwt token
        String token = JwtUtil.encode(AuthcConst.TOKEN_EXPIRE, params);
        ResponseUtil.write(response, ResultUtil.success(token));
    }
}
