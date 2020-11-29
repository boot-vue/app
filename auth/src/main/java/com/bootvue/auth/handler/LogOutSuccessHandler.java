package com.bootvue.auth.handler;

import com.bootvue.auth.model.AppToken;
import com.bootvue.auth.util.ResponseUtil;
import com.bootvue.common.mapper.UserMapper;
import com.bootvue.common.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogOutSuccessHandler implements LogoutSuccessHandler {

    private final UserMapper userMapper;
    private final RedisTemplate<String, AppToken> redisTemplate;

    @Autowired
    public LogOutSuccessHandler(RedisTemplate<String, AppToken> redisTemplate, UserMapper userMapper) {
        this.redisTemplate = redisTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 验证token   暂时什么也不做
        /*String token = request.getHeader("token");
        Long userId = Long.valueOf(request.getHeader("userId"));
        if (StringUtils.isEmpty(token) || ObjectUtils.isEmpty(userId)) {
            ResponseUtil.write(response, ResultUtil.error(ResultCode.PARAM_ERROR));
        }

        User user = userDao.findById(userId).get();
        // 验证token
        Claims claims = JwtUtil.decode(token);
        String username = claims.get("username", String.class);
        if (!user.getUsername().equalsIgnoreCase(username)) {
            ResponseUtil.write(response, ResultUtil.error(ResultCode.PARAM_ERROR));
        }

        // 清理redis
        redisTemplate.delete("token:user_" + userId);*/
        ResponseUtil.write(response, ResultUtil.success());
    }
}
