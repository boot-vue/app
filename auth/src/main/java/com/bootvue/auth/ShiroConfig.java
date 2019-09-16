package com.bootvue.auth;

import com.bootvue.auth.cache.RedisCacheManager;
import com.bootvue.auth.realm.UserRealm;
import com.bootvue.auth.session.RedisSessionDao;
import com.bootvue.auth.session.RedisSessionManager;
import com.bootvue.common.config.AppConfig;
import com.bootvue.common.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro 配置
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShiroConfig {
    private final RedisSessionDao redisSessionDao;
    private final RedisCacheManager redisCacheManager;
    private final AppConfig appConfig;
    private final UserDao userDao;

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean
    public UserRealm userRealm(HashedCredentialsMatcher matcher) {
        UserRealm userRealm = new UserRealm(userDao);
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        filter.setSecurityManager(securityManager);
        // 设置未授权提示Url
        filter.setUnauthorizedUrl("/auth/authorization");
        //未认证url
        filter.setLoginUrl("/auth/authentication");

        /**
         * anon：匿名用户可访问
         * authc：认证用户可访问
         * user：使用rememberMe可访问
         * perms：对应权限可访问
         * roles[xxx]：对应角色权限可访问
         **/
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/login", "anon");
        //添加白名单
        List<String> allowUrl = appConfig.getAllowUrl();
        if (!CollectionUtils.isEmpty(allowUrl)) {
            for (String url : allowUrl) {
                filterMap.put(url, "anon");
            }
        }

        filterMap.put("/**", "authc");
        filterMap.put("/logout", "logout");

        filter.setFilterChainDefinitionMap(filterMap);
        return filter;
    }

    /**
     * 注入 securityManager
     */
    @Bean
    public SecurityManager defaultWebSecurityManager(HashedCredentialsMatcher hashedCredentialsMatcher) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联realm.
        securityManager.setRealm(userRealm(hashedCredentialsMatcher));
        //session manager
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    //session manager
    @Bean
    public SessionManager sessionManager() {
        RedisSessionManager redisSessionManager = new RedisSessionManager();
        redisSessionManager.setSessionDAO(redisSessionDao);
        //自定义cache manager
        redisSessionManager.setCacheManager(redisCacheManager);
        return redisSessionManager;
    }

}
