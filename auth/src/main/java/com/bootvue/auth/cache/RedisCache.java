package com.bootvue.auth.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义 shiro缓存
 *
 * @param <K>
 * @param <V>
 */
@Component
public class RedisCache<K, V> implements Cache<K, V> {
    private static final Long expire = 4L;  //缓存时间  4小时
    @Resource
    private RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public V get(K k) throws CacheException {
        if (ObjectUtils.isEmpty(k)) {
            return null;
        }
        V v = (V) SerializationUtils.deserialize(redisTemplate.opsForValue().get(k));
        return v;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        redisTemplate.opsForValue().set(String.valueOf(k), SerializationUtils.serialize(v), expire, TimeUnit.HOURS);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        V v = (V) SerializationUtils.deserialize(redisTemplate.opsForValue().get(k));
        if (!ObjectUtils.isEmpty(v)) {
            redisTemplate.delete(String.valueOf(k));
        }
        return v;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
