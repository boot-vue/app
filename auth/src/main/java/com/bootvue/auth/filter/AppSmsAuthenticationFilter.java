package com.bootvue.auth.filter;

import com.bootvue.auth.exception.CaptchaException;
import com.bootvue.auth.model.AppSms;
import com.bootvue.auth.model.AppSmsToken;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信登录
 */
public class AppSmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final StringRedisTemplate stringRedisTemplate;
    private boolean postOnly = true;

    public AppSmsAuthenticationFilter(StringRedisTemplate template) {
        super(new AntPathRequestMatcher("/login/sms", HttpMethod.POST.toString()));
        this.stringRedisTemplate = template;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String phone = this.obtainPhone(request);
            String code = this.obtainCode(request);

            if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
                throw new UsernameNotFoundException("手机号 验证码不能为空");
            }

            String smsCodeKey = "code:sms_" + phone.trim();

            String smsCode = stringRedisTemplate.opsForValue().get(smsCodeKey);
            if (StringUtils.isEmpty(smsCode) || !smsCode.equalsIgnoreCase(code)) {
                throw new CaptchaException("手机验证码错误");
            }

            stringRedisTemplate.delete(smsCodeKey);

            AppSmsToken authRequest = new AppSmsToken(new AppSms(phone.trim()));
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter("phone");
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter("code");
    }

    protected void setDetails(HttpServletRequest request, AppSmsToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
