package com.bootvue.auth.authc;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AppUserToken extends UsernamePasswordAuthenticationToken {
    private String userType = "";


    public AppUserToken(AppUser appUser) {
        super(appUser.getUsername(), appUser.getPassword());
        this.userType = appUser.getType();
    }
}
