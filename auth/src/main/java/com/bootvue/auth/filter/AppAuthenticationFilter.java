package com.bootvue.auth.filter;

import com.alibaba.fastjson.JSON;
import com.bootvue.common.result.ResultCode;
import com.bootvue.common.result.ResultUtil;
import io.netty.util.CharsetUtil;
import org.apache.http.entity.ContentType;
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
public class AppAuthenticationFilter extends BasicAuthenticationFilter {
    public AppAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // todo 拦截 or  放行

        // 校验token有效性
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            response.setCharacterEncoding(CharsetUtil.UTF_8.toString());

            response.getWriter().write(JSON.toJSONString(ResultUtil.error(ResultCode.AUTHEN_ERROR)));
            return;
        }

        //放行  --> 下面各自认证provider处理认证
        chain.doFilter(request, response);
    }
}
