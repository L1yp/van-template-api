package org.cloud.cache.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.cache.CacheTTL;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Order(-1) // 需要比redis serializer 提前执行
public class CacheTTLAop {


    @Around("@annotation(org.cloud.cache.CacheTTL) || @within(org.cloud.cache.CacheTTL)")
    public Object run(JoinPoint joinPoint) throws Throwable {
        try {
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature methodSignature) {
                Method method = methodSignature.getMethod();
                CacheTTL expireAt = method.getAnnotation(CacheTTL.class);
                if (expireAt == null) {
                    Class<?> declaringType = methodSignature.getDeclaringType();
                    expireAt = declaringType.getAnnotation(CacheTTL.class);
                }
                long ttl = expireAt.value();
                CacheTTLContext.setTTL(ttl);
            }
            return ((MethodInvocationProceedingJoinPoint) joinPoint).proceed(joinPoint.getArgs());
        } finally {
            CacheTTLContext.remove();
        }
    }

}
