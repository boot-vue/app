package com.bootvue.auth.session;

import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.shiro.web.servlet.ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE;

/**
 * 自定义session 会话管理器
 */
public class RedisSessionManager extends DefaultWebSessionManager {
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        //如果请求头中有 Authorization 则其值为sessionId
        if (!Strings.isBlank(id)) {
            request.setAttribute(REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }

}
