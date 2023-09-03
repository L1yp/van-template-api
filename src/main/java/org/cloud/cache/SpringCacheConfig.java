package org.cloud.cache;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.cloud.cache.custom.CustomRedisCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class SpringCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(JavaTimeModule javaTimeModule){
        JacksonRedisSerializer<?> serializer = new JacksonRedisSerializer<>(javaTimeModule);
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
//                .disableCachingNullValues();
    }


    @Bean
    @Primary
    public CustomRedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory, RedisCacheConfiguration redisCacheConfiguration) {
        // 使用分布式锁
        return new CustomRedisCacheManager(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory), redisCacheConfiguration);
    }


    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        /**
         * com.github.benmanes.caffeine.cache.CaffeineSpec#configure(java.lang.String, java.lang.String)
         * initialCapacity=1,maximumSize=1,maximumWeight=2,weakKeys=1,weakValues=1,softValues=1,expireAfterAccess=2,expireAfterWrite=2,refreshAfterWrite=1,recordStats=1
         */
//        caffeineCacheManager.setCaffeineSpec(CaffeineSpec.parse(""));
        caffeineCacheManager.setAllowNullValues(false);
        return caffeineCacheManager;
    }



}
