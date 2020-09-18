package com.bootvue.auth.authc;

import com.bootvue.common.dao.UserDao;
import com.bootvue.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * UserDetailsService
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppUserDetailService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new AppUserDetails(user.getId(), username, user.getPassword(),
                user.getStatus() == 0, AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
    }
}
