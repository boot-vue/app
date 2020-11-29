package com.bootvue.auth;

import com.bootvue.auth.authc.AppUserDetailService;
import com.bootvue.auth.encoder.Md5PasswordEncoder;
import com.bootvue.auth.filter.AppAuthenticationFilter;
import com.bootvue.auth.filter.AppSmsAuthenticationFilter;
import com.bootvue.auth.filter.AppUsernamePasswordAuthenticationFilter;
import com.bootvue.auth.handler.AccessFailHandler;
import com.bootvue.auth.handler.FailHandler;
import com.bootvue.auth.handler.LogOutSuccessHandler;
import com.bootvue.auth.handler.SuccessHandler;
import com.bootvue.auth.model.AppToken;
import com.bootvue.auth.provider.AppAuthenticationProvider;
import com.bootvue.auth.provider.JwtAuthenticationProvider;
import com.bootvue.auth.provider.SmsAuthenticationProvider;
import com.bootvue.common.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Md5PasswordEncoder md5PasswordEncoder;
    private final AppUserDetailService appUserDetailService;
    private final SuccessHandler successHandler;
    private final FailHandler failHandler;
    private final LogOutSuccessHandler logOutSuccessHandler;
    private final AccessFailHandler accessFailHandler;
    private final RedisTemplate<String, AppToken> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final AppConfig appConfig;
    private final UserDao userDao;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .addFilterBefore(appUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(appSmsAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginProcessingUrl("/login")
                .and().authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("admin")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(logOutSuccessHandler)
                .and()
                .addFilter(new AppAuthenticationFilter(authenticationManager(), appConfig, redisTemplate))
                .exceptionHandling()
                .accessDeniedHandler(accessFailHandler)
                .and().csrf().disable();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        // 认证provider
        AppAuthenticationProvider daoProvider = new AppAuthenticationProvider(md5PasswordEncoder, appUserDetailService);
        SmsAuthenticationProvider smsProvider = new SmsAuthenticationProvider(userDao);
        JwtAuthenticationProvider jwtProvider = new JwtAuthenticationProvider();

        ProviderManager providerManager = new ProviderManager(Arrays.asList(daoProvider, smsProvider, jwtProvider));
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    protected AppUsernamePasswordAuthenticationFilter appUsernamePasswordAuthenticationFilter() throws Exception {
        AppUsernamePasswordAuthenticationFilter filter = new AppUsernamePasswordAuthenticationFilter(stringRedisTemplate);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failHandler);
        return filter;
    }

    protected AppSmsAuthenticationFilter appSmsAuthenticationFilter() throws Exception {
        AppSmsAuthenticationFilter filter = new AppSmsAuthenticationFilter(stringRedisTemplate);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failHandler);
        return filter;
    }

}
