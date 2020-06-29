package com.bootvue.auth.filter;

import com.bootvue.auth.model.AppToken;
import com.bootvue.auth.model.JwtToken;
import com.bootvue.auth.util.ResponseUtil;
import com.bootvue.common.config.AppConfig;
import com.bootvue.common.result.ResultCode;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 拦截 需要认证 or 授权的url
 */
@Slf4j
public class AppAuthenticationFilter extends BasicAuthenticationFilter {
    private static final PathMatcher MATCHER = new AntPathMatcher();
    private final AppConfig appConfig;
    private final RedisTemplate<String, AppToken> redisTemplate;

    public AppAuthenticationFilter(AuthenticationManager authenticationManager, AppConfig appConfig, RedisTemplate<String, AppToken> redisTemplate) {
        super(authenticationManager);
        this.appConfig = appConfig;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = request.getRequestURI();

        List<String> allowList = appConfig.getAllowList(); //白名单放行 不校验
        if (!CollectionUtils.isEmpty(allowList)) {
            for (String item : allowList) {
                if (MATCHER.match(item, uri)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        // 校验token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token) || !JwtUtil.isVerify(token)) {
            ResponseUtil.write(response, ResultUtil.error(ResultCode.TOKEN_ERROR));
            return;
        }

        // *************token验证通过
        Claims params = JwtUtil.decode(token);
        log.debug("用户信息: {}", params);

        Long userId = Long.valueOf(String.valueOf(params.get("user_id")));
        AppToken appToken = redisTemplate.opsForValue().get("token:user_" + userId);
        if (StringUtils.isEmpty(appToken)) {
            ResponseUtil.write(response, ResultUtil.error(ResultCode.TOKEN_ERROR));
            return;
        }

        JwtToken authToken = new JwtToken(String.valueOf(params.get("username")), userId,
                AuthorityUtils.commaSeparatedStringToAuthorityList(String.valueOf(params.get("authorities"))));
        Authentication authResult = this.getAuthenticationManager().authenticate(authToken);
        // context上下文注入 已认证的 对象信息
        SecurityContextHolder.getContext().setAuthentication(authResult);

        //放行  --> provider处理认证
        chain.doFilter(request, response);
    }

}
