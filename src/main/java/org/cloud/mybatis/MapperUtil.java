package org.cloud.mybatis;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MapperUtil implements InitializingBean {

    @Resource
    Map<String, Mapper<?, ?>> mapperMap;


    private final static Map<Type, Mapper<?, ?>> entityMapperMap = new ConcurrentHashMap<>(64);
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Set<Entry<String, Mapper<?, ?>>> entries = mapperMap.entrySet();
        for (Entry<String, Mapper<?, ?>> entry : entries) {
            Mapper<?, ?> mapper = entry.getValue();
            List<Type> types = getGenericInterfaces(mapper.getClass());
            for (Type type : types) {
                if (type instanceof ParameterizedType parameterizedType) {
                    if (parameterizedType.getRawType().equals(Mapper.class)) {
                        Type entityClazz = parameterizedType.getActualTypeArguments()[0];
                        entityMapperMap.put(entityClazz, mapper);
                        break;
                    }
                }
            }
        }
    }

    private List<Type> getGenericInterfaces(Class<?> clazz) {
        Set<Type> interfaces = new LinkedHashSet<>();
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        LinkedList<Type> candidateTypes = new LinkedList<>(Arrays.asList(genericInterfaces));
        while (!candidateTypes.isEmpty()) {
            Type type = candidateTypes.pop();
            interfaces.add(type);
            if (type instanceof Class<?> currentClazz) {
                Type[] clazzGenericInterfaces = currentClazz.getGenericInterfaces();
                candidateTypes.addAll(Arrays.asList(clazzGenericInterfaces));
            }
        }

        return new ArrayList<>(interfaces);
    }

    @SuppressWarnings("unchecked")
    public static <T, PK extends Serializable> Mapper<T, PK> getMapper(Type entityClazz) {
        return (Mapper<T, PK>) entityMapperMap.get(entityClazz);
    }
    
}
