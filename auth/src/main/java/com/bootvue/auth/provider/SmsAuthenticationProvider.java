package com.bootvue.auth.provider;

import com.bootvue.auth.authc.AppSms;
import com.bootvue.auth.authc.AppSmsToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 短信 认证
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AppSmsToken smsToken = (AppSmsToken) authentication;

        String phone = String.valueOf(smsToken.getCredentials());
        String code = String.valueOf(smsToken.getPrincipal());

        // 判断 用户是否存在

        /*if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
*/
        AppSmsToken authenticationResult = new AppSmsToken(new AppSms(phone, code));

        authenticationResult.setDetails(smsToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AppSmsToken.class.isAssignableFrom(authentication);
    }

}
