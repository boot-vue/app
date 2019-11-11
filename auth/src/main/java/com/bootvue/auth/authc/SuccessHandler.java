package com.bootvue.auth.authc;

import com.bootvue.auth.util.JsonUtil;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回token
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Map<String, Object> params = new HashMap<>();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        params.put("username", userDetails.getUsername());
        params.put("authorities", userDetails.getAuthorities());
        String token = JwtUtil.encode(Duration.ofDays(30L).toMillis(), params);
        JsonUtil.write(response, ResultUtil.success(token));
    }
}
