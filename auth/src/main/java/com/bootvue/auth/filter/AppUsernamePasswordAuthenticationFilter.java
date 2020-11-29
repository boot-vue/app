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
            String code = request.getParameter("code");
            String key = request.getParameter("key");
            String tenantCode = request.getParameter("tenant_code");

            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(tenantCode)) {
                // 用户名 密码为空
                throw new UsernameNotFoundException("参数错误");
            }

            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(key)) {
                throw new CaptchaException("验证码不能为空");
            }

            String captcha = stringRedisTemplate.opsForValue().get("captcha:line_" + key);
            if (StringUtils.isEmpty(captcha) || !captcha.equalsIgnoreCase(code)) {
                throw new CaptchaException("图形验证码错误");
            }

            stringRedisTemplate.delete(key);

            AppUserDetails userDetails = new AppUserDetails(null, username.trim(), password.trim(), tenantCode.trim(), "", "", true, AuthorityUtils.NO_AUTHORITIES);
            AppUserToken authRequest = new AppUserToken(userDetails);
            authRequest.setDetails(userDetails);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
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
