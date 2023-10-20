package org.cloud.mybatis;

import io.mybatis.mapper.BaseMapper;
import io.mybatis.provider.Caching;
import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.SqlScript;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Mapper<T, I extends Serializable> extends BaseMapper<T, I> {


    @Lang(Caching.class)
    @InsertProvider(type = Provider.class, method = "insertList")
    int insertList(@Param("list") Collection<T> list);


    class Provider {
        /**
         * 批量保存实体
         *
         * @param providerContext 上下文
         * @return cacheKey
         */
        public static String insertList(ProviderContext providerContext, @Param("list") Collection<?> list) {
            return SqlScript.caching(providerContext, entity -> "INSERT INTO " + entity.tableName()
                    + "(" + entity.insertColumnList() + ")"
                    + " VALUES " +
                    IntStream.range(0, list.size())
                            .boxed()
                            .map(it -> "(" + entity.insertColumns()
                                    .stream()
                                    .map(column -> column.variables("list[" + it + "]."))
                                    .collect(Collectors.joining(",")) + ")")
                            .collect(Collectors.joining(","))
                    );
        }
    }

}
