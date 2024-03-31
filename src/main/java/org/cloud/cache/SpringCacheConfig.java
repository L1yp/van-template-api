package org.cloud.cache;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

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
                    if (k instanceof String key && StringUtils.isNotBlank(key) && key.contains("#")) {
                        String ttl = StringUtils.substringAfterLast(key, "#");
                        try {
                            return Duration.parse(ttl);
                        } catch (Exception ignored) { }
                    }
                    return null;
                });
    }


}
