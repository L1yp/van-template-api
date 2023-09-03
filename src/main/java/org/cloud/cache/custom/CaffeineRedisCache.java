package org.cloud.cache.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;

@Getter
@Setter
public class CaffeineRedisCache extends AbstractValueAdaptingCache {

    /**
     * 一级缓存，若启用一级缓存则设置一个非空实例，反之则设置为null
     */
    protected Cache firstCache;

    /**
     * 二级缓存
     */
    protected Cache secondCache;

    /**
     * Create an {@code AbstractValueAdaptingCache} with the given setting.
     *
     * @param allowNullValues whether to allow for {@code null} values
     */
    public CaffeineRedisCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    @Override
    protected Object lookup(Object key) {
        ValueWrapper valueWrapper = null;
        if (firstCache != null) {
            valueWrapper = firstCache.get(key);
        }
        if (valueWrapper == null) {
            if (secondCache == null) {
                return null;
            }
            valueWrapper = secondCache.get(key);
            // 若二级缓存存在，但一级缓存被启用并且不存在key，则put到一级缓存
            if (valueWrapper != null) {
                if (firstCache != null) {
                    firstCache.put(key, valueWrapper.get());
                }
            } else {
                return null;
            }
        }
        return valueWrapper.get();

    }

    @Override
    public String getName() {
        if (firstCache != null) {
            return firstCache.getName();
        }
        if (secondCache != null) {
            return secondCache.getName();
        }
        throw new RuntimeException("Please provide a valid cache.");
    }

    @Override
    public Object getNativeCache() {
        if (firstCache != null) {
            return firstCache.getNativeCache();
        }
        if (secondCache != null) {
            return secondCache.getNativeCache();
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        if (firstCache != null) {
            T t = firstCache.get(key, valueLoader);
            if (t != null) {
                return t;
            }
        }
        if (secondCache != null) {
            return secondCache.get(key, valueLoader);
        }
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        if (firstCache != null) {
            firstCache.put(key, value);
        }
        if (secondCache != null) {
            secondCache.put(key, value);
        }
    }

    @Override
    public void evict(Object key) {
        if (firstCache != null) {
            firstCache.evict(key);
        }
        if (secondCache != null) {
            secondCache.evict(key);
        }
    }

    @Override
    public void clear() {
        if (firstCache != null) {
            firstCache.clear();
        }
        if (secondCache != null) {
            secondCache.clear();
        }
    }

}
