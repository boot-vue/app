package com.bootvue.auth.filter;

import com.alibaba.fastjson.JSON;
import com.bootvue.auth.jwt.JwtToken;
import com.bootvue.auth.jwt.JwtUtil;
import com.bootvue.common.type.Result;
import com.bootvue.common.type.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends AccessControlFilter {
    /*
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String token = getToken(request);

        //验证token是否合法  合法--> 刷新token有效时间 _->true
        try {
            if (!StringUtils.isEmpty(token) && JwtUtil.isVerify(token)) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 返回结果为true表明  登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String jwt = getToken(servletRequest);
        JwtToken jwtToken = new JwtToken(jwt);

        try {
            // 委托 realm 进行登录认证
            //最终还是调用JwtRealm进行的认证
            getSubject(servletRequest, servletResponse).login(jwtToken);
            //也就是subject.login(token)
            return true;
        } catch (Exception e) {
            // 响应错误信息
            errorResponse(servletResponse, JSON.toJSONString(new Result<>(ResultCode.AUTHEN_ERROR.getCode(), ResultCode.AUTHEN_ERROR.getMsg(), null)));
        }

        return false;
    }

    private void errorResponse(ServletResponse response, String jsonResult) throws IOException {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.SC_OK);
        httpResponse.getWriter()
                .write(jsonResult);
    }

    // 获取token
    private String getToken(ServletRequest request) {
        return ((HttpServletRequest) request).getHeader("token");
    }
}
