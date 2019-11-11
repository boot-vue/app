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
    private String username;
    private String password;

    public AppUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
        this.username = username;
        this.password = password;
    }
}
