package org.cloud.cache;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class SpringCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(JavaTimeModule javaTimeModule){
        JacksonRedisSerializer<?> serializer = new JacksonRedisSerializer<>(javaTimeModule);
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//                .computePrefixWith(key -> key + ":")
                // set ttl
                .entryTtl((k, v) -> {
                    if (k instanceof String key && StringUtils.isNotBlank(key)) {
                        String namespace = StringUtils.substringBefore(key, CacheKeyPrefix.SEPARATOR);
                        return RedisKeyConstants.prefixDurationMap.get(namespace);
                    }
                    return null;
                });
    }


}
