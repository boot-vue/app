package com.bootvue.auth.provider;

import com.bootvue.auth.authc.AppUserDetails;
import com.bootvue.auth.model.AppSmsToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * 短信 认证
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {
    private final UserDao userDao;

    public SmsAuthenticationProvider(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AppSmsToken smsToken = (AppSmsToken) authentication;

        String phone = String.valueOf(smsToken.getPrincipal());

        User user = userDao.findByPhone(phone);
        if (user == null) {
            throw new InternalAuthenticationServiceException("用户不存在");
        }

        AppUserDetails userDetails = new AppUserDetails(user.getId(), user.getUsername(), user.getPassword(), true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
        AppSmsToken authenticationResult = new AppSmsToken(userDetails, AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
        authenticationResult.setDetails(userDetails);
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AppSmsToken.class.isAssignableFrom(authentication);
    }

}
