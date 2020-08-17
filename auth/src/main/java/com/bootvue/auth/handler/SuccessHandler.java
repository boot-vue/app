package com.bootvue.auth.handler;

import cn.hutool.core.util.IdUtil;
import com.bootvue.auth.authc.AppUserDetails;
import com.bootvue.auth.model.AppToken;
import com.bootvue.auth.util.ResponseUtil;
import com.bootvue.common.dao.UserDao;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final RedisTemplate<String, AppToken> redisTemplate;

    @Autowired
    public SuccessHandler(RedisTemplate<String, AppToken> redisTemplate, UserDao userDao) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 返回token
        Map<String, Object> params = new HashMap<>();
        AppUserDetails userDetails = (AppUserDetails) authentication.getDetails();
        params.put("user_id", userDetails.getUserId());
        params.put("username", userDetails.getUsername());
        Collection<GrantedAuthority> authorities = userDetails.getAuthorities();
        String key = "token:user_" + userDetails.getUserId();
        String authoritiesStr = "";

        if (!CollectionUtils.isEmpty(authorities)) {
            StringBuffer buffer = new StringBuffer();
            // 转成 , 分割
            authorities.forEach(e -> buffer.append(e.getAuthority()).append(","));
            authoritiesStr = buffer.toString().substring(0, buffer.toString().length() - 1);
        }
        params.put("authorities", authoritiesStr);

        // 判断是否redis 已存在 user_id
        AppToken appToken = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(appToken)) {

            // jwt token
            String accessToken = JwtUtil.encode(Duration.ofSeconds(7200L).toMillis(), params);
            String refreshToken = IdUtil.randomUUID();

            // access_token  7200s
            // refresh_token 30d
            appToken = new AppToken(userDetails.getUserId(), accessToken, refreshToken, userDetails.getUsername(), authoritiesStr);

            // user完整对象
            redisTemplate.opsForValue().set(key, appToken, 30L, TimeUnit.DAYS);
        } else {
            // access_token  7200s
            // refresh_token 30d
            String accessToken = JwtUtil.encode(Duration.ofSeconds(7200L).toMillis(), params);

            appToken.setAccessToken(accessToken);
            appToken.setAuthorities(authoritiesStr);
            Long expire = redisTemplate.getExpire(key);

            // user完整对象
            redisTemplate.opsForValue().set(key, appToken, expire, TimeUnit.SECONDS);
        }

        ResponseUtil.write(response, ResultUtil.success(appToken));
    }
}
