package org.cloud.mybatis.mapper;

import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.EntityField;
import io.mybatis.provider.EntityTable;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.cloud.mybatis.typehandler.JacksonTypeHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class CustomEntityTable extends EntityTable {
    protected CustomEntityTable(Class<?> entityClass) {
        super(entityClass);
    }

    public static EntityTable of(Class<?> entityClass) {
        return new CustomEntityTable(entityClass);
    }

    @Override
    protected ResultMap genResultMap(Configuration configuration, ProviderContext providerContext, String cacheKey) {
        List<ResultMapping> resultMappings = new ArrayList<>();
        for (EntityColumn entityColumn : selectColumns()) {
            String column = entityColumn.column();
            //去掉可能存在的分隔符，例如：`order`
            Matcher matcher = DELIMITER.matcher(column);
            if (matcher.find()) {
                column = matcher.group(1);
            }
            ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.property(), column, entityColumn.javaType());
            if (entityColumn.jdbcType() != null && entityColumn.jdbcType() != JdbcType.UNDEFINED) {
                builder.jdbcType(entityColumn.jdbcType());
            }
            if (entityColumn.typeHandler() != null && entityColumn.typeHandler() != UnknownTypeHandler.class) {
                try {
                    TypeHandler typeHandlerInstance = getTypeHandlerInstance(entityColumn.javaType(), entityColumn.typeHandler());
                    Type fieldGenericType = getFieldGenericType(entityColumn);
                    if (typeHandlerInstance instanceof JacksonTypeHandler jacksonTypeHandler) {
                        jacksonTypeHandler.setFieldType(fieldGenericType);
                    }
                    builder.typeHandler(typeHandlerInstance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            List<ResultFlag> flags = new ArrayList<>();
            if (entityColumn.id()) {
                flags.add(ResultFlag.ID);
            }
            builder.flags(flags);
            resultMappings.add(builder.build());
        }
        String resultMapId = generateResultMapId(providerContext, RESULT_MAP_NAME);
        ResultMap.Builder builder = new ResultMap.Builder(configuration, resultMapId, entityClass(), resultMappings, true);
        return builder.build();
    }

    public Type getFieldGenericType(EntityColumn entityColumn) {
        try {
            Field rawField = EntityField.class.getDeclaredField("field");
            rawField.setAccessible(true);
            Field columnField = (Field) rawField.get(entityColumn.field());
            return columnField.getGenericType();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
