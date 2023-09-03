package org.cloud.cache;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.cloud.cache.aop.CacheResultTypeContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;
import java.lang.reflect.Type;

public class JacksonRedisSerializer<T> implements RedisSerializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JacksonRedisSerializer(JavaTimeModule module) {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.registerModule(module);
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        Type returnType = CacheResultTypeContext.getReturnType();
        if (returnType == null) {
            throw new RuntimeException("请先配置返回值类型");
        }

        JavaType javaType = objectMapper.constructType(returnType);

        try {
            return objectMapper.readValue(bytes, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
