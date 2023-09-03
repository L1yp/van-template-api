package org.cloud.cache.aop;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;

public class CacheTTLContext {


    private static final NamedThreadLocal<Long> local = new NamedThreadLocal<>("cache-ttl");

    /**
     * @param ttl 秒数
     */
    public static void setTTL(Long ttl) {
        local.set(ttl);
    }

    @Nullable
    public static Long getTTL() {
        return local.get();
    }


    public static void remove() {
        local.remove();
    }

}
