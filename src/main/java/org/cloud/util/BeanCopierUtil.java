package org.cloud.util;

import org.cloud.model.enums.base.BasicEnum;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BeanCopierUtil {

    private static final ConcurrentHashMap<BeanCopierKey, BeanCopier> CACHE = new ConcurrentHashMap<>(128);

    @SuppressWarnings("unchecked")
    public static void copy(Object source, Object target) {
        Class<?> sourceClazz = source.getClass();
        Class<?> targetClazz = target.getClass();
        BeanCopierKey beanCopierKey = BeanCopierKey.buildKey(sourceClazz, targetClazz);
        BeanCopier beanCopier = CACHE.computeIfAbsent(beanCopierKey, k -> BeanCopier.create(sourceClazz, targetClazz, true));
        beanCopier.copy(source, target, new BeanCopierConverter());
    }

    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClazz) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }

        Class<?> sourceClazz = sourceList.getFirst().getClass();
        BeanCopierKey beanCopierKey = BeanCopierKey.buildKey(sourceClazz, targetClazz);
        BeanCopier beanCopier = CACHE.computeIfAbsent(beanCopierKey, k -> BeanCopier.create(sourceClazz, targetClazz, true));

        List<T> result = new ArrayList<>();
        for (Object source : sourceList) {
            T t = null;
            try {
                t = targetClazz.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            beanCopier.copy(source, t, new BeanCopierConverter());
            result.add(t);
        }

        return result;
    }

    private static class BeanCopierConverter implements Converter {

        @Override
        public Object convert(Object value, Class targetClass, Object context) {
            if (value == null) {
                return null;
            }
            if (targetClass.isAssignableFrom(value.getClass())) {
                return value;
            }
            // 若 source field是枚举 targetField不是枚举，则设置为value
            else if (value instanceof BasicEnum basicEnum && value.getClass().isEnum()) {
                if (!targetClass.isEnum()) {
                    return basicEnum.getValue();
                }
            }
            // 若source不是枚举，targetField是枚举，则需要转换为枚举
            else if (!value.getClass().isEnum() && targetClass.isEnum() && BasicEnum.class.isAssignableFrom(targetClass)) {
                BasicEnum[] enumValues = (BasicEnum[]) targetClass.getEnumConstants();
                for (BasicEnum enumValue : enumValues) {
                    if (Objects.equals(enumValue.getValue(), value)) {
                        return enumValue;
                    }
                }
            }
            return null;
        }
    }

    private static class BeanCopierKey {
        public Class<?> sourceClazz;
        public Class<?> targetClazz;

        private BeanCopierKey() {}

        public static BeanCopierKey buildKey(Class<?> sourceClazz, Class<?> targetClazz) {
            BeanCopierKey bean = new BeanCopierKey();
            bean.sourceClazz = sourceClazz;
            bean.targetClazz = targetClazz;
            return bean;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof BeanCopierKey && ((BeanCopierKey) obj).sourceClazz.equals(sourceClazz) && ((BeanCopierKey) obj).targetClazz.equals(targetClazz);
        }
    }

}
