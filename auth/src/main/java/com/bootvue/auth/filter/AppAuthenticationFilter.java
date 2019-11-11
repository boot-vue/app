package com.bootvue.auth.filter;

import com.bootvue.auth.util.JsonUtil;
import com.bootvue.common.result.ResultCode;
import com.bootvue.common.result.ResultUtil;
import com.bootvue.utils.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截 需要认证 or 授权的url
 */
@Slf4j
public class AppAuthenticationFilter extends BasicAuthenticationFilter {
    public AppAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 无需身份认证的url直接放行

        // 校验token有效性
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            JsonUtil.write(response, ResultUtil.error(ResultCode.AUTHEN_ERROR));
            return;
        }

        if (!JwtUtil.isVerify(token)) {
            // token无效
            JsonUtil.write(response, ResultUtil.error(ResultCode.TOKEN_ERROR));
            return;
        }

        // *************token验证通过

        Claims params = JwtUtil.decode(token);
        log.info("用户信息: {}", params);
        // 这里可以添加权限校验


        //放行  --> 下面各自认证provider处理认证
        chain.doFilter(request, response);
    }
}
