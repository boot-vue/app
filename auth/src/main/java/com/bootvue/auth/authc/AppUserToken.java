package com.bootvue.auth.authc;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AppUserToken extends UsernamePasswordAuthenticationToken {
    public AppUserToken(AppUserDetails appUser) {
        super(appUser.getUsername(), appUser.getPassword());
    }

    public AppUserToken(AppUserDetails appUser, Collection<? extends GrantedAuthority> authorities) {
        super(appUser.getUsername(), appUser.getPassword(), authorities);
        super.setAuthenticated(true);
    }
}
