package com.bootvue.controller.user;

import com.bootvue.auth.jwt.JwtUtil;
import com.bootvue.common.entity.User;
import com.bootvue.common.type.AppException;
import com.bootvue.common.type.ResultCode;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证  授权
 */
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @RequestMapping("/login")
    public String login(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        String jwtToken = "";
        try {
            subject.login(usernamePasswordToken);

            //生成jwt token
            Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());

            jwtToken = JwtUtil.encode(Duration.ofDays(30L).toMillis(), params);
        } catch (AuthenticationException e) {//  需要 细分异常类型  可以catch AuthenticationException子类异常
            throw new AppException(ResultCode.LOGIN_ERROR);
        }
        return jwtToken;
    }

    /**
     * 退出登录
     */
    @RequestMapping("/logout")
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 认证失败  跳转这里
     */
    @RequestMapping("/auth/authentication")
    public void authenticationError() {
        // 认证失败  redis中的临时session最好清除掉
        throw new AppException(ResultCode.AUTHENTICATION_ERROR);
    }

    /**
     * 没有权限 跳转这里
     */
    @RequestMapping("/auth/authorization")
    public void authorizationError() {
        throw new AppException(ResultCode.AUTHORIZATION_ERROR);
    }

}
