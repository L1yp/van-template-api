package org.cloud.cache;

import java.util.Collection;

public interface ICachePutOperation {

    /**
     * 生成缓存key
     * @return 缓存key列表
     */
    Collection<String> genKeys();

    /**
     * 若缓存命中则不执行CachePut操作，否则执行
     * @return 是否缓存命中
     */
    boolean isCacheHit();

}
