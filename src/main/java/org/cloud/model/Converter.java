package org.cloud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.cloud.util.BeanCopierUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Converter<T> {

    default T convert() {
        // 获取泛型类型 T 的 Class 对象
        Class<T> targetType = getTargetType();
        if (targetType == null) {
            return null;
        }
        try {
            T t = targetType.getDeclaredConstructor().newInstance();
            BeanCopierUtil.copy(this, t);
//            BeanUtils.copyProperties(this, t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Set<Class<?>> directConvertorImplClazz = new HashSet<>(List.of(
            AbstractModel.class, AbstractOutputDTO.class, AbstractUpdateDTO.class, AbstractWithUpdateModel.class,
            AbstractWithUpdateOutputDTO.class, AbstractTreeOutputDTO.class, AbstractWithUpdateTreeOutputDTO.class
    ));

    @JsonIgnore
    default ParameterizedType getCurrentGenericInterface() {
        Class<?> superClazz = getClass();
        Type type = null;
        while (!superClazz.equals(Object.class)) {
            Type[] genericInterfaces = superClazz.getGenericInterfaces();
            Optional<Type> optionalType = Arrays.stream(genericInterfaces).filter(it -> ParameterizedType.class.isAssignableFrom(it.getClass())).filter(it -> ((ParameterizedType) it).getRawType().equals(Converter.class)).findFirst();
            if (optionalType.isEmpty()) {
                Type genericSuperClazzType = superClazz.getGenericSuperclass();
                if (ParameterizedType.class.isAssignableFrom(genericSuperClazzType.getClass())) {
                    if (directConvertorImplClazz.contains(superClazz.getSuperclass())) {
                        type = genericSuperClazzType;
                        break;
                    }
                }

                superClazz = superClazz.getSuperclass();
                continue;
            }
            type = optionalType.get();
            break;
        }
        return (ParameterizedType) type;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    default Class<T> getTargetType() {
        ParameterizedType parameterizedType = getCurrentGenericInterface();
        if (parameterizedType == null) {
            return null;
        }
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }


}
