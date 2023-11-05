package org.cloud.mybatis;

import io.mybatis.provider.Entity.Table;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperProxy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析Mapper中返回值类型
 */
@Slf4j
@Component
public class MapperAutoResultMap implements InitializingBean {

    @Resource
    List<Mapper<?, ?>> mapperList;

    @Getter
    private final List<Class<?>> candidateBuildResultMapTypeList = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        for (Mapper<?, ?> mapper : mapperList) {
            if (!Proxy.isProxyClass(mapper.getClass())) {
                continue;
            }
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(mapper);
            if (invocationHandler instanceof MapperProxy<?> mapperProxy) {
                Field field = ReflectionUtils.findField(MapperProxy.class, "mapperInterface");
                if (field == null) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    // 获取mapper类型
                    Class<? extends Mapper<?, ?>> mapperClazz = (Class<? extends Mapper<?, ?>>) field.get(mapperProxy);
                    // 获取mapper的第一个泛型参数类型
                    Class<?> mapperDOType = getMapperDOType(mapperClazz);
                    if (mapperDOType == null) {
                        continue;
                    }
                    Method[] methods = mapperClazz.getMethods();
                    for (Method declaredMethod : methods) {
                        // 获取方法所在定义类
                        Class<?> declaringClass = declaredMethod.getDeclaringClass();
                        // 查询mapper中声明的对象中的resultMap
                        if (declaringClass != mapperClazz) {
                            continue;
                        }
                        // 获取方法的泛型返回值
                        Type genericReturnType = declaredMethod.getGenericReturnType();
                        // 若返回值是泛型
                        if (genericReturnType instanceof ParameterizedType parameterizedType) {
                            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                            // 处理泛型参数有且仅有一个的返回值类型
                            if (actualTypeArguments == null || actualTypeArguments.length != 1) {
                                continue;
                            }
                            Type actualTypeArgument = actualTypeArguments[0];
                            if (actualTypeArgument == mapperDOType) {
                                continue;
                            }
                            if (actualTypeArgument instanceof Class<?> genericType) {
                                if (genericType.isAnnotationPresent(Table.class)) {
                                    candidateBuildResultMapTypeList.add(genericType);
                                }

                            }
                        }
                        // 返回值是直接类型
                        else if (genericReturnType instanceof Class<?> returnClazz) {
                            // 返回值是DO类型，无需重复生成result map
                            if (returnClazz == mapperDOType) {
                                continue;
                            }
                            if (returnClazz.isAnnotationPresent(Table.class)) {
                                candidateBuildResultMapTypeList.add(returnClazz);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public Class<?> getMapperDOType(Class<? extends Mapper<?, ?>> mapperClazz) {
        Type[] genericInterfaces = mapperClazz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getRawType() == Mapper.class || parameterizedType.getRawType() == io.mybatis.mapper.Mapper.class) {
                    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

}
