package com.bootvue.auth;

import com.bootvue.auth.filter.JwtFilter;
import com.bootvue.auth.jwt.JwtSubjectFactory;
import com.bootvue.auth.realm.UserRealm;
import com.bootvue.common.config.AppConfig;
import com.bootvue.common.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro 配置
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShiroConfig {
    private final AppConfig appConfig;
    private final UserDao userDao;

    // jwt subjectFactory  关闭session
    @Bean
    public SubjectFactory subjectFactory() {
        return new JwtSubjectFactory();
    }

    // 加密方式
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
    public Realm realm(HashedCredentialsMatcher matcher) {
        UserRealm userRealm = new UserRealm(userDao);
        userRealm.setCachingEnabled(false);
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

        // jwt filter
        Map<String, Filter> map = new HashMap<>();
        map.put("jwt", new JwtFilter());
        filter.setFilters(map);

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
        List<String> whiteList = appConfig.getWhitelist();
        if (!CollectionUtils.isEmpty(whiteList)) {
            for (String url : whiteList) {
                filterMap.put(url, "anon");
            }
        }


        filterMap.put("/logout", "logout");
        filterMap.put("/**", "jwt");  //所有请求经过jwt filter

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
        securityManager.setRealm(realm(hashedCredentialsMatcher));
        // jwt  不需要sessionManager
        // 关闭 subject dao
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //禁止Subject的getSession方法
        securityManager.setSubjectFactory(subjectFactory());
        return securityManager;
    }

}
