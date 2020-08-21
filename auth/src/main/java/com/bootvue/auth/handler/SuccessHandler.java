package com.bootvue.auth.handler;

import cn.hutool.core.util.IdUtil;
import com.bootvue.auth.authc.AppUserDetails;
import com.bootvue.auth.model.AppToken;
import com.bootvue.auth.util.ResponseUtil;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import com.google.common.base.Joiner;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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

    private final RedissonClient redissonClient;

    @Autowired
    public SuccessHandler(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
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
        String authoritiesStr = Joiner.on(',').skipNulls().join(authorities);
        params.put("authorities", authoritiesStr);

        // jwt token
        String accessToken = JwtUtil.encode(Duration.ofSeconds(7200L).toMillis(), params);
        String refreshToken = IdUtil.randomUUID();

        // access_token  7200s
        // refresh_token 30d
        AppToken appToken = new AppToken(userDetails.getUserId(), accessToken, refreshToken, userDetails.getUsername(), authoritiesStr);

        RSetCache<String> refreshTokenSet = redissonClient.getSetCache(String.format("refresh_token:user_%s", userDetails.getUserId()));
        RSetCache<AppToken> accessTokenSet = redissonClient.getSetCache(String.format("access_token:user_%s", userDetails.getUserId()));

        refreshTokenSet.add(refreshToken, 30L, TimeUnit.DAYS);
        accessTokenSet.add(appToken, 2L, TimeUnit.HOURS);

        ResponseUtil.write(response, ResultUtil.success(appToken));
    }
}
