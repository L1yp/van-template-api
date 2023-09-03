package org.cloud.cache.aop;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;

import java.lang.reflect.Type;

public class CacheResultTypeContext {

    private static final NamedThreadLocal<Type> local = new NamedThreadLocal<>("cache-result-type");

    public static void setType(Type type) {
        local.set(type);
    }

    @Nullable
    public static Type getReturnType() {
        return local.get();
    }


    public static void remove() {
        local.remove();
    }


}
