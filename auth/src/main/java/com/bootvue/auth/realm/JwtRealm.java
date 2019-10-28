package com.bootvue.auth.realm;

import com.bootvue.auth.jwt.JwtToken;
import com.bootvue.auth.jwt.JwtUtil;
import com.bootvue.common.entity.User;
import com.bootvue.common.type.AppException;
import com.bootvue.common.type.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtRealm extends AuthorizingRealm {
    /*
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
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

    //认证
    //这个token就是从过滤器中传入的jwtToken
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String jwt = (String) token.getPrincipal();
        if (StringUtils.isEmpty(jwt)) {
            throw new AppException(ResultCode.AUTHENTICATION_ERROR);
        }
        //判断
        JwtUtil jwtUtil = new JwtUtil();
        if (!jwtUtil.isVerify(jwt)) {
            throw new UnknownAccountException();
        }
        //下面是验证这个user是否是真实存在的
        //String username = (String) jwtUtil.decode(jwt).get("username");//判断数据库中username是否存在
        // 可以不判断  isVerify会验证 jwt是否有效

        return new SimpleAuthenticationInfo(jwt, jwt, getName());
    }

}
