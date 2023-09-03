package org.cloud.cache.custom;

import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义默认cache resolver和 cacheManager
 */
@Configuration
public class CustomSpringCacheConfig implements CachingConfigurer {

    @Resource
    CaffeineCacheManager caffeineCacheManager;

    @Resource
    CustomRedisCacheManager redisCacheManager;

    /**
     * 多级缓存解析器
     */
    @Override
    public CacheResolver cacheResolver() {
        return new MultipleCacheResolver(redisCacheManager, caffeineCacheManager);
    }

    /**
     * 默认使用redis缓存
     */
    @Override
    public CacheManager cacheManager() {
        return redisCacheManager;
    }
}
