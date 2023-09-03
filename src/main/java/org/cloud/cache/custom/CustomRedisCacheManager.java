package org.cloud.cache.custom;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * 支持可配置化的本地缓存
 */
public class CustomRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfiguration;

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    @Override
    @NotNull
    protected RedisCache createRedisCache(@NotNull String name, RedisCacheConfiguration cacheConfig) {
        return new CustomRedisCache(name, this.cacheWriter, cacheConfig != null ? cacheConfig : this.defaultCacheConfiguration);
    }
}
