package com.bootvue.controller.authentication;

import com.alibaba.fastjson.JSON;
import com.bootvue.auth.model.AppToken;
import com.bootvue.common.result.AppException;
import com.bootvue.common.result.Result;
import com.bootvue.common.result.ResultCode;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenController {
    private final RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/refresh_token")
    public Result<AppToken> refreshToken(@RequestParam(required = true, name = "refresh_token") String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new AppException(ResultCode.PARAM_ERROR);
        }
        String appTokenStr = redisTemplate.opsForValue().get("token:" + refreshToken);
        AppToken appToken = JSON.parseObject(appTokenStr, AppToken.class);

        Map<String, Object> params = new HashMap<>();
        params.put("username", appToken.getUsername());
        params.put("authorities", AuthorityUtils.commaSeparatedStringToAuthorityList(appToken.getAuthorities()));

        // jwt token
        String accessToken = JwtUtil.encode(Duration.ofSeconds(7200L).toMillis(), params);

        appToken.setAccessToken(accessToken);
        Long expire = redisTemplate.getExpire("token:" + refreshToken);
        if (expire == null) {
            expire = 0L;
        }
        redisTemplate.opsForValue().set("token:" + refreshToken, JSON.toJSONString(appToken), expire, TimeUnit.SECONDS);
        return ResultUtil.success(appToken);
    }
}
