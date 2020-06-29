package com.bootvue.auth.filter;

import com.bootvue.auth.authc.AppUserDetails;
import com.bootvue.auth.exception.CaptchaException;
import com.bootvue.auth.model.AppUserToken;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AuthenticationFilter
 */
public class AppUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final boolean postOnly = true;
    private final StringRedisTemplate stringRedisTemplate;

    public AppUsernamePasswordAuthenticationFilter(StringRedisTemplate template) {
        this.stringRedisTemplate = template;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            String code = this.obtainCaptcha(request);

            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                // 用户名 密码为空
                throw new UsernameNotFoundException("用户名密码不能为空");
            }

            if (StringUtils.isEmpty(code)) {
                throw new CaptchaException("验证码不能为空");
            }

            String key = "captcha:line_" + code;

            String captcha = stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.isEmpty(captcha)) {
                throw new CaptchaException("验证码错误");
            }

            stringRedisTemplate.delete(key);

            username = username.trim();
            password = password.trim();

            AppUserDetails userDetails = new AppUserDetails(null, username, password, AuthorityUtils.NO_AUTHORITIES);
            AppUserToken authRequest = new AppUserToken(userDetails);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    private String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter("captcha");
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return super.obtainPassword(request);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return super.obtainUsername(request);
    }

    @Override
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        super.setDetails(request, authRequest);
    }

    @Override
    public void setUsernameParameter(String usernameParameter) {
        super.setUsernameParameter(usernameParameter);
    }

    @Override
    public void setPasswordParameter(String passwordParameter) {
        super.setPasswordParameter(passwordParameter);
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        super.setPostOnly(postOnly);
    }
}
