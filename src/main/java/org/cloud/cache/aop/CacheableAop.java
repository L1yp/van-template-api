package org.cloud.cache.aop;

import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.cache.ICachePutOperation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;

@Aspect
@Component
public class CacheableAop {

    @Resource
    CacheManager cacheManager;

    @Around(value = "@annotation(org.springframework.cache.annotation.Cacheable)")
    public Object run(JoinPoint joinPoint) throws Throwable {
        String[] cacheNames = new String[0];
        if (joinPoint.getSignature() instanceof MethodSignature signature) {
            Class<?> declaringType = signature.getDeclaringType();
            if (declaringType.isAnnotationPresent(CacheConfig.class)) {
                CacheConfig annotation = declaringType.getAnnotation(CacheConfig.class);
                cacheNames = annotation.cacheNames();
            }
            if (signature.getMethod().isAnnotationPresent(Cacheable.class)) {
                Cacheable cacheable = signature.getMethod().getAnnotation(Cacheable.class);
                if (!CollectionUtils.isEmpty(Arrays.asList(cacheable.cacheNames()))) {
                    cacheNames = cacheable.cacheNames();
                }
            }
        }
        Object result = ((MethodInvocationProceedingJoinPoint) joinPoint).proceed(joinPoint.getArgs());
        if (result instanceof ICachePutOperation cachePutOperation && cacheNames.length > 0) {
            // 非缓存命中，则需要Put
            if (!cachePutOperation.isCacheHit()) {
                Collection<String> keys = cachePutOperation.genKeys();
                for (String cacheName : cacheNames) {
                    Cache cache = cacheManager.getCache(cacheName);
                    if (cache != null) {
                        keys.forEach(k -> cache.put(k, result));
                    }
                }
            }
        }
        return result;

    }



}
