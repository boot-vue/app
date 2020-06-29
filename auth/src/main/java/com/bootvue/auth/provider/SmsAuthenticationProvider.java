package com.bootvue.auth.provider;

import com.bootvue.auth.authc.AppUserDetails;
import com.bootvue.auth.model.AppSmsToken;
import com.bootvue.auth.model.AppUserToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

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
        AppUserDetails userDetails = new AppUserDetails(null, "demo", "2222", AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user"));
        AppUserToken authenticationResult = new AppUserToken(userDetails);
        authenticationResult.setDetails(userDetails);
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AppSmsToken.class.isAssignableFrom(authentication);
    }

}
