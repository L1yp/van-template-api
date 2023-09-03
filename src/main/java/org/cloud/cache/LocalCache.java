package org.cloud.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cacheable和CacheEvict必须配置一致，如TTL一下，本地缓存配置一致
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LocalCache {

    /**
     * 仅使用本地缓存
     */
    boolean onlyLocal() default false;
    
}
