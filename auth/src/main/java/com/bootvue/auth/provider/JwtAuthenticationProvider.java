package com.bootvue.auth.provider;

import com.bootvue.auth.authc.AppUserDetailService;
import com.bootvue.auth.model.JwtToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * jwt-token
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AppUserDetailService userDetailService;

    public JwtAuthenticationProvider(AppUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //处理jwt token认证
        JwtToken jwtToken = (JwtToken) authentication;
        String username = String.valueOf(jwtToken.getCredentials());

        // redis缓存先查询user是否存在
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        return new JwtToken(userDetails.getUsername(), jwtToken.getPrincipal(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtToken.class.isAssignableFrom(authentication);
    }

}
