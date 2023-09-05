package org.cloud.cache.custom;

import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.validation.constraints.NotNull;
import org.cloud.cache.CacheTTL;
import org.cloud.cache.LocalCache;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MultipleCacheResolver implements CacheResolver {

    private final CustomRedisCacheManager redisCacheManager;
    private final CaffeineCacheManager caffeineCacheManager;

    /**
     * 相同过期策略可使用同一个Caffeine
     */
    private final Map<Long, Caffeine<Object, Object>> ttlCaffeineMap = new ConcurrentHashMap<>(64);

    /**
     * Cache无需重复创建，缓存相同CacheName的Cache对象
     */
    private final Map<String, CaffeineCache> caffeineTTLCacheMap = new ConcurrentHashMap<>(64);

    public MultipleCacheResolver(CustomRedisCacheManager redisCacheManager, CaffeineCacheManager caffeineCacheManager) {
        this.redisCacheManager = redisCacheManager;
        this.caffeineCacheManager = caffeineCacheManager;
    }

    @Override
    @NotNull
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
         Collection<String> cacheNames = context.getOperation().getCacheNames();
        Class<?> targetClazz = context.getTarget().getClass();
        if (CollectionUtils.isEmpty(cacheNames)) {
            return Collections.emptyList();
        }

        long clazzTTL = -1;
        if (targetClazz.isAnnotationPresent(CacheTTL.class)) {
            CacheTTL cacheTTL = targetClazz.getAnnotation(CacheTTL.class);
            if (cacheTTL != null) {
                clazzTTL = cacheTTL.value();
            }
        }
        // FIXME: 缓存注解和驱逐缓存的TTL配置和LocalCache配置不一致时可能会取到不同的Cache导致数据异常
        Method method = context.getMethod();
        Collection<Cache> result = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {

            CaffeineRedisCache caffeineRedisCache = new CaffeineRedisCache(true);
            boolean isOnlyLocalCache = false;
            // 启用本地缓存
            if (method.isAnnotationPresent(LocalCache.class)) {
                LocalCache localCache = method.getAnnotation(LocalCache.class);
                isOnlyLocalCache = localCache.onlyLocal(); // 是否只使用本地缓存
                if (method.isAnnotationPresent(CacheTTL.class)) { // 有TTL
                    CacheTTL cacheTTL = context.getMethod().getAnnotation(CacheTTL.class);
                    clazzTTL = cacheTTL.value();
                }

                if (clazzTTL > 0) {
                    // 相同的TTL不必重复创建Caffeine, 取同一个即可
                    Caffeine<Object, Object> caffeine = ttlCaffeineMap.computeIfAbsent(clazzTTL, key -> Caffeine.newBuilder().expireAfterAccess(key, TimeUnit.SECONDS));

                    // (TTL, Caffeine)
                    // (CacheName, CaffeineCache)
                    // 缓存cacheName -> CaffeineCache
                    // 若同一个CacheName，不同的TTL，这里是有问题的，key应该是cacheName + TTL
                    CaffeineCache caffeineCache = caffeineTTLCacheMap.computeIfAbsent(cacheName + clazzTTL, k -> new CaffeineCache(cacheName, caffeine.build(), true));

                    caffeineRedisCache.setFirstCache(caffeineCache);
                }

                // 若已设置有TTL的Cache则不使用CaffeineManager的Cache对象
                if (caffeineRedisCache.getFirstCache() == null) {
                    Cache caffeineCache = caffeineCacheManager.getCache(cacheName);
                    if (caffeineCache == null) {
                        throw new IllegalArgumentException("Cannot find cache named '" +
                                cacheName + "' for " + context.getOperation());
                    }
                    caffeineRedisCache.setFirstCache(caffeineCache);
                }

            }


            // 非仅本地缓存，则需要创建redis二级缓存
            if (!isOnlyLocalCache) {
                Cache redisCache = redisCacheManager.getCache(cacheName);
                if (redisCache == null) {
                    throw new IllegalArgumentException("Cannot find cache named '" +
                            cacheName + "' for " + context.getOperation());
                }
                caffeineRedisCache.setSecondCache(redisCache);
            }

            result.add(caffeineRedisCache);
        }
        return result;
    }
}