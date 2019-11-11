package com.bootvue.auth.filter;

import com.bootvue.auth.authc.AppSms;
import com.bootvue.auth.authc.AppSmsToken;
import com.bootvue.auth.util.AuthcConst;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信登录
 */
public class AppSmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_PHONE_KEY = AuthcConst.PHONE_KEY;
    public static final String SPRING_SECURITY_FORM_CODE_KEY = AuthcConst.PHONE_CODE;
    private boolean postOnly = true;

    public AppSmsAuthenticationFilter() {
        super(new AntPathRequestMatcher(AuthcConst.SMS_LOGIN_URL, HttpMethod.POST.toString()));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String phone = this.obtainPhone(request);
            String code = this.obtainCode(request);
            if (phone == null) {
                phone = "";
            }

            if (code == null) {
                code = "";
            }

            AppSmsToken authRequest = new AppSmsToken(new AppSms(phone, code));
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_PHONE_KEY);
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_CODE_KEY);
    }

    protected void setDetails(HttpServletRequest request, AppSmsToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
