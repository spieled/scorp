package cn.studease.util.cache;

import java.util.Map;

/**
 * 缓存相关工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public interface CacheUtil {

    /**
     * 将value关联到key，存入缓存
     *
     * @param key
     * @param value
     */
    public void set(CacheKeyBuilder.CacheKey key, Object value);

    public void set(CacheKeyBuilder.CacheKey key, Object value, long expireTime);

    public void mset(Map<CacheKeyBuilder.CacheKey, Object> map, boolean pipeline);

    public void mset(Map<CacheKeyBuilder.CacheKey, Object> map, long expireTime, boolean pipeline);

    public boolean exist(CacheKeyBuilder.CacheKey key);

    public Object get(CacheKeyBuilder.CacheKey key);

}
