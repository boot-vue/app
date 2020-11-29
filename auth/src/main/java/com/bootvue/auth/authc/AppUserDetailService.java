package com.bootvue.auth.authc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootvue.common.entity.User;
import com.bootvue.common.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String usernameAndTenantCode) throws UsernameNotFoundException {
        String[] strings = usernameAndTenantCode.split(" ");
        String username = strings[0];
        String tenantCode = strings[1];

        User user = userMapper.selectOne(new QueryWrapper<User>().lambda()
                .eq(User::getUsername, username).eq(User::getTenantCode, tenantCode)
        );

        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("参数错误");
        }
        return new AppUserDetails(user.getId(), username, user.getPassword(),
                user.getTenantCode(), user.getAvatar(), user.getPhone(), user.getStatus(), AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
    }
}
