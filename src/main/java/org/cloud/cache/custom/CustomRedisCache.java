package org.cloud.cache.custom;

import jakarta.validation.constraints.NotNull;
import org.cloud.cache.aop.CacheResultTypeContext;
import org.cloud.cache.aop.CacheTTLContext;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;

public class CustomRedisCache extends RedisCache {

    private final RedisCacheWriter cacheWriter;
    private final String name;
    private final RedisCacheConfiguration cacheConfig;

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected CustomRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheConfig = cacheConfig;
    }


    @Override
    public void put(@NotNull Object key, Object value) {

        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {

            throw new IllegalArgumentException(String.format(
                    "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                    name));
        }

        // add expire ttl
        Duration ttl = cacheConfig.getTtl();
        Long ctxTTL = CacheTTLContext.getTTL();
        if (ctxTTL != null && ctxTTL > 0) {
            ttl = Duration.ofSeconds(ctxTTL);
        }

        // FIXME: 处理cacheValue可能为null的问题
        this.cacheWriter.put(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue), ttl);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {

        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }

        // add expire ttl
        Duration ttl = cacheConfig.getTtl();
        Long ctxTTL = CacheTTLContext.getTTL();
        if (ctxTTL != null && ctxTTL > 0) {
            ttl = Duration.ofSeconds(ctxTTL);
        }

        // FIXME: 处理cacheValue可能为null的问题
        byte[] result = cacheWriter.putIfAbsent(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue),
                ttl);

        if (result == null) {
            return null;
        }

        return new SimpleValueWrapper(fromStoreValue(deserializeCacheValue(result)));
    }


    private byte[] createAndConvertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }


    @Override
    public <T> T get(Object key, Class<T> type) {
        CacheResultTypeContext.setType(type);
        return super.get(key, type);
    }
}
