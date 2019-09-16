package com.bootvue.auth.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 自定义session 会话dao
 */
@Configuration
@Slf4j
public class RedisSessionDao extends CachingSessionDAO {
    //token默认时长  30天
    private static final Long EXPIRE_TOKEN = 30L;
    @Resource
    private RedisTemplate<String, byte[]> redisTemplate;

    @Override
    protected Serializable doCreate(Session session) {
        //session id可以 重写规则
        String sessionId = String.valueOf(generateSessionId(session));
        redisTemplate.opsForValue().set(sessionId, SerializationUtils.serialize(session), EXPIRE_TOKEN, TimeUnit.DAYS);
        //绑定  session 与sessionId
        assignSessionId(session, sessionId);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        //cache缓存中有  直接从缓存取
        //这里默认试内存缓存   自定义缓存要重写cachemanager
        Session session = getCachedSession(sessionId);
        if (session == null) {
            session = (Session) SerializationUtils.deserialize(redisTemplate.opsForValue().get(sessionId));
        }

        if (session != null) {
            //缓存session
            assignSessionId(session, sessionId);
            cache(session, sessionId);
            //每次请求是否刷新  session有效期
            redisTemplate.expire(String.valueOf(sessionId), EXPIRE_TOKEN, TimeUnit.DAYS);
        }
        return session;
    }


    @Override
    protected void doUpdate(Session session) {
        redisTemplate.opsForValue().set(String.valueOf(session.getId()), SerializationUtils.serialize(session));
        //刷新  session有效时间
        redisTemplate.expire(String.valueOf(session.getId()), EXPIRE_TOKEN, TimeUnit.DAYS);
        //缓存
        assignSessionId(session, session.getId());
        cache(session, session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        //session 已被销毁
    }

}
