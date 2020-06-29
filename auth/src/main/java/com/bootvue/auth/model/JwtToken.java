package com.bootvue.auth.model;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class JwtToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String username;   // 用户名
    private Long userId;

    public JwtToken(Claims params) {
        super(null);
        this.username = String.valueOf(params.get("username"));
        this.userId = Long.valueOf(String.valueOf(params.get("user_id")));
        setAuthenticated(false);
    }

    public JwtToken(String username, Long userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        this.userId = userId;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return this.username;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
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
        userId = null;
        username = null;
    }
}