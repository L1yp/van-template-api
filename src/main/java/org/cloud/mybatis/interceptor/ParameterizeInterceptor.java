package org.cloud.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.type.TypeHandler;
import org.cloud.mybatis.typehandler.JacksonTypeHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


@Intercepts({
        @Signature(
                type = ParameterHandler.class,
                method = "setParameters",
                args = { PreparedStatement.class }
        ),
})
@Slf4j
@Component
public class ParameterizeInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        if (Proxy.isProxyClass(parameterHandler.getClass())) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(parameterHandler);
            if (invocationHandler instanceof Plugin) {
                Field targetField = ReflectionUtils.findField(invocationHandler.getClass(), "target");
                if (targetField != null) {
                    targetField.setAccessible(true);
                    parameterHandler = (ParameterHandler) targetField.get(invocationHandler);
                }
            }
        }

        Object parameterObject = parameterHandler.getParameterObject();
        resetJacksonTypeHandler(parameterHandler, parameterObject);
        return invocation.proceed();
    }

    public void resetJacksonTypeHandler(ParameterHandler parameterHandler, Object parameterObject) {
        // 在setParameter前处理typeHandler
        if (!(parameterHandler instanceof DefaultParameterHandler)) {
            throw new RuntimeException("ParameterHandler被替换，需要适配新的ParameterHandler才可以使用自适应的JacksonHandler");
        }

        Field field = ReflectionUtils.findField(DefaultParameterHandler.class, "boundSql");
        if (field == null) {
            return;
        }
        if (!field.canAccess(parameterHandler)) {
            field.trySetAccessible();
        }
        BoundSql boundSql = (BoundSql) ReflectionUtils.getField(field, parameterHandler);
        if (boundSql == null) {
            return;
        }
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (ParameterMapping parameterMapping : parameterMappings) {
            TypeHandler<?> typeHandler = parameterMapping.getTypeHandler();
            if (typeHandler instanceof JacksonTypeHandler jacksonTypeHandler) {
                Field typeField = ReflectionUtils.findField(parameterObject.getClass(), parameterMapping.getProperty());
                if (typeField == null) {
                    throw new RuntimeException("无法获取JacksonTypeHandler的实际类型信息");
                }
                Type genericType = typeField.getGenericType();
                jacksonTypeHandler.setFieldType(genericType);
            }
        }

        Map<String, Object> additionalParameters = boundSql.getAdditionalParameters();
        Set<Entry<String, Object>> entries = additionalParameters.entrySet();
        for (Entry<String, Object> entry : entries) {
            String key = entry.getKey();
        }

    }



}
