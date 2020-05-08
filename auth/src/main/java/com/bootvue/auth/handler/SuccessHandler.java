package com.bootvue.auth.handler;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.bootvue.auth.authc.AppUserDetails;
import com.bootvue.auth.model.AppToken;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.netty.util.CharsetUtil;
import org.apache.http.entity.ContentType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final RedisTemplate<String, String> redisTemplate;

    public SuccessHandler(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回token
        Map<String, Object> params = new HashMap<>();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        params.put("username", userDetails.getUsername());
        params.put("authorities", userDetails.getAuthorities());

        // jwt token
        String accessToken = JwtUtil.encode(Duration.ofSeconds(7200L).toMillis(), params);
        String refreshToken = IdUtil.randomUUID();

        // access_token  7200s
        // refresh_token 30d
        AppToken appToken = new AppToken(accessToken, refreshToken, 7200L, userDetails.getUsername(), userDetails.getAuthorities().toString());

        redisTemplate.opsForValue().set("token:" + refreshToken, JSON.toJSONString(appToken), 30L, TimeUnit.DAYS);

        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(CharsetUtil.UTF_8.toString());
        response.getWriter().write(new JsonMapper().writeValueAsString(ResultUtil.success(appToken)));
    }
}
