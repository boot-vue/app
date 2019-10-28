package com.bootvue.auth.realm;


import com.bootvue.common.dao.UserDao;
import com.bootvue.common.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRealm extends AuthorizingRealm {
    private final UserDao userDao;

    public static void main(String[] args) {
        SimpleHash simpleHash = new SimpleHash("md5", "123456", ByteSource.Util.bytes("demo1"), 1024);
        System.out.println(simpleHash.toString());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //  只处理 UsernamePasswordToken登录
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 用户授权
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user != null) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            // 角色与权限字符串集合
            List<String> rolesCollection = Arrays.asList(user.getRoles().split(","));

            authorizationInfo.addRoles(rolesCollection);
            return authorizationInfo;
        }
        return null;
    }

    /**
     * 用户认证
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        User user = userDao.findByUsername(usernamePasswordToken.getUsername());

        if (user == null) {
            throw new UnknownAccountException();
        }

        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUsername());
        return new SimpleAuthenticationInfo(user, user.getPassword(), credentialsSalt, getName());
    }

}
