package org.cloud.mybatis.mapper;

import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.EntityTable;
import io.mybatis.provider.SqlSourceCustomize;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.TypeHandler;
import org.cloud.model.AbstractModel;
import org.cloud.model.AbstractWithUpdateModel;
import org.cloud.mybatis.typehandler.JacksonTypeHandler;
import org.cloud.util.JSON;
import org.cloud.web.context.LoginUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 处理mybatis-mapper的BaseMapper中内置方法的JacksonTypeHandler通用类型
 */
@Slf4j
public class CustomSqlSourceCustomize implements SqlSourceCustomize {

    private static final String CREATE_BY = "createBy";
    private static final String UPDATE_BY = "updateBy";

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";


    @Override
    public SqlSource customize(SqlSource sqlSource, EntityTable entity, MappedStatement ms, ProviderContext context) {
        return parameterObject -> {
            LocalDateTime now = LocalDateTime.now();
            String loginUserId = LoginUtils.getLoginUserId();

            Set<String> excludeFields = Set.of(entity.excludeFields());
            SqlCommandType sqlCommandType = ms.getSqlCommandType();
            if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
                MetaObject metaObject = ms.getConfiguration().newMetaObject(parameterObject);
                if (parameterObject instanceof AbstractModel<?> && sqlCommandType == SqlCommandType.INSERT) {
                    setCommonField(excludeFields, metaObject, CREATE_BY, loginUserId);
                    setCommonField(excludeFields, metaObject, UPDATE_BY, loginUserId);
                    setCommonField(excludeFields, metaObject, UPDATE_TIME, now);
                    setCommonField(excludeFields, metaObject, CREATE_TIME, now);
                }
                if (sqlCommandType == SqlCommandType.UPDATE) {
                    if (parameterObject instanceof AbstractWithUpdateModel<?>) {
                        setCommonField(excludeFields, metaObject, UPDATE_BY, loginUserId);
                        setCommonField(excludeFields, metaObject, UPDATE_TIME, now);
                    } else {
                        // 处理非AbstractWithUpdateModel子类也需要自动填充的字段
                        setCommonField(excludeFields, metaObject, UPDATE_BY, loginUserId);
                        setCommonField(excludeFields, metaObject, UPDATE_TIME, loginUserId);
                    }
                }
            }


            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

            for (ParameterMapping parameterMapping : parameterMappings) {
                String property = parameterMapping.getProperty();
                // wrapper的sql参数
                if (boundSql.hasAdditionalParameter(property)) {
                    if (property.endsWith(".value")) {
                        String entityFieldName = (String) boundSql.getAdditionalParameter(property.replace(".value", ".condition"));
                        List<EntityColumn> columns = entity.columns();
                        Optional<EntityColumn> entityColumn = columns.stream().filter(it -> it.column().equals(entityFieldName)).findFirst();
                        if (entityColumn.isPresent()) {
                            Class<? extends TypeHandler> typeHandlerClazz = entityColumn.get().typeHandler();
                            if (JacksonTypeHandler.class.equals(typeHandlerClazz)) {
                                Object additionalParameter = boundSql.getAdditionalParameter(property);
                                String strValue = JSON.toJSONString(additionalParameter);
                                boundSql.setAdditionalParameter(property, strValue);
                            }
                        }
                    }



                }


                if (parameterMapping.getTypeHandler() instanceof JacksonTypeHandler jacksonTypeHandler) {
                    Field field = ReflectionUtils.findField(parameterObject.getClass(), property);
                    if (field == null) {
                        throw new RuntimeException("无法获取JacksonTypeHandler的实际类型信息");
                    }
                    Type genericType = field.getGenericType();
                    jacksonTypeHandler.setFieldType(genericType);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("parameterMappings: {}", parameterMappings);
            }

            return boundSql;
        };
    }

    private void setCommonField(Set<String> excludeFields, MetaObject metaObject, String field, Object value) {
        if (excludeFields.contains(field) || !metaObject.hasGetter(field)) {
            return;
        }
        Object createBy = metaObject.getValue(field);
        if (createBy == null) {
            if (metaObject.hasSetter(field)) {
                metaObject.setValue(field, value);
            }
        }
    }
}
