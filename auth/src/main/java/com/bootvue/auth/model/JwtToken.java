package com.bootvue.auth.model;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class JwtToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private Object credentials;   // 用户名
    private Object principal;

    public JwtToken(Claims params) {
        super(null);
        this.credentials = String.valueOf(params.get("username"));
        this.principal = params;
        setAuthenticated(false);
    }

    public JwtToken(Object credentials, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
        principal = null;
    }
}