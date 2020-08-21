package com.bootvue.controller.authentication;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.bootvue.auth.model.AppToken;
import com.bootvue.common.dao.UserDao;
import com.bootvue.common.entity.User;
import com.bootvue.common.result.AppException;
import com.bootvue.common.result.Result;
import com.bootvue.common.result.ResultCode;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/auth")
@Api(tags = "auth相关")
@Slf4j
public class AuthController {
    private static final LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
    private final RedissonClient redissonClient;
    private final StringRedisTemplate redisTemplate;
    private final UserDao userDao;

    @ApiOperation("换取新的token")
    @GetMapping("/refresh_token")
    public Result<AppToken> refreshToken(@RequestParam(name = "refresh_token") String refreshToken,
                                         @RequestParam(name = "user_id") Long userId) throws JsonProcessingException {
        if (StringUtils.isEmpty(refreshToken) || ObjectUtils.isEmpty(userId)) {
            throw new AppException(ResultCode.PARAM_ERROR);
        }

        RSetCache<String> refreshTokenSet = redissonClient.getSetCache(String.format("refresh_token:user_%s", userId));

        boolean exist = CollectionUtils.contains(refreshTokenSet.iterator(), refreshToken);
        if (!exist) {
            throw new AppException(ResultCode.REFRESH_TOKEN_ERROR);
        }

        User user = userDao.findById(userId).orElseThrow(() -> new AppException(ResultCode.PARAM_ERROR));

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("username", user.getUsername());
        String authoritiesStr = Joiner.on(',').skipNulls().join(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
        params.put("authorities", authoritiesStr);

        // jwt token
        String accessToken = JwtUtil.encode(Duration.ofSeconds(7200L).toMillis(), params);
        AppToken appToken = new AppToken(userId, accessToken, refreshToken, user.getUsername(), authoritiesStr);

        RSetCache<AppToken> accessTokenSet = redissonClient.getSetCache(String.format("access_token:user_%s", userId));
        accessTokenSet.add(appToken, 2L, TimeUnit.HOURS);

        return ResultUtil.success(appToken);
    }

    @ApiOperation("获取图形验证码")
    @GetMapping("/captcha")
    public Result<String> captcha() {
        lineCaptcha.createCode();
        String code = lineCaptcha.getCode();
        String image = "data:image/png;base64," + lineCaptcha.getImageBase64();
        redisTemplate.opsForValue().set("captcha:line_" + code, code, 10, TimeUnit.MINUTES);
        return ResultUtil.success(image);
    }

    @ApiOperation("获取短信验证码")
    @GetMapping("/sms")
    public Result smsCode(@RequestParam("phone") String phone) {
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set("code:sms_" + phone, code, 15, TimeUnit.MINUTES);
        log.info("短信验证码 : {}", code);
        return ResultUtil.success();
    }
}
