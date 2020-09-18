package com.bootvue.auth.authc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 *
 */
@Getter
@Setter
public class AppUserDetails extends User {
    private Long userId;
    private String username;
    private String password;
    private boolean enabled;

    public AppUserDetails(Long userId, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}
