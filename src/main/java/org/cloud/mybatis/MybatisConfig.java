package org.cloud.mybatis;

import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.EntityFactory;
import io.mybatis.provider.EntityTable;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.mybatis.typehandler.JacksonTypeHandler;
import org.cloud.web.service.system.IConfigService;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

@Slf4j
@org.springframework.context.annotation.Configuration
public class MybatisConfig implements ConfigurationCustomizer, ApplicationListener<ContextRefreshedEvent> {

    private final ConcurrentHashMap<Type, JacksonTypeHandler> handlerMap = new ConcurrentHashMap<>(64);

    private Configuration configuration;

    @Resource
    IConfigService configService;

    @Resource
    MapperAutoResultMap mapperAutoResultMap;

    @Override
    public void customize(Configuration configuration) {
        this.configuration = configuration;
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        List<Class<?>> classList = configService.getBasicEnumClassList();
        classList.forEach(clazz -> typeHandlerRegistry.register(clazz, BasicEnumTypeHandler.class));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 自动构建resultMap
        autoBuildResultMap(configuration, mapperAutoResultMap.getCandidateBuildResultMapTypeList());

        resetTypeHandlerInResultMapping();
    }

    public void resetTypeHandlerInResultMapping() {
        Collection<ResultMap> resultMaps = configuration.getResultMaps();
        for (Object object : resultMaps) {
            if (object instanceof ResultMap resultMap) {
                Class<?> type = resultMap.getType();
                List<ResultMapping> resultMappings = resultMap.getResultMappings();
                List<ResultMapping> propertyResultMappings = resultMap.getPropertyResultMappings();
                ArrayList<ResultMapping> mappingList = new ArrayList<>(resultMappings);
                mappingList.addAll(propertyResultMappings);
                for (ResultMapping resultMapping : mappingList) {
                    if (resultMapping.getTypeHandler().getClass().equals(JacksonTypeHandler.class)) {
                        Field field = ReflectionUtils.findField(type, resultMapping.getProperty());
                        if (field == null) {
                            continue;
                        }
                        Type fieldType = field.getGenericType();
                        JacksonTypeHandler typeHandler = handlerMap.computeIfAbsent(fieldType, JacksonTypeHandler::new);
                        MetaObject metaObject = configuration.newMetaObject(resultMapping);
                        metaObject.setValue("typeHandler", typeHandler);
                    }
                }
            }

        }
    }

    public void autoBuildResultMap(Configuration configuration, List<Class<?>> resultTypeList) {
        for (Class<?> clazz : resultTypeList) {
            EntityTable entityTable = EntityFactory.create(clazz);
            ResultMap resultMap = genResultMap(configuration, entityTable);
            configuration.addResultMap(resultMap);
        }
    }

    public ResultMap genResultMap(Configuration configuration, EntityTable entityTable) {
        List<ResultMapping> resultMappings = new ArrayList<>();
        for (EntityColumn entityColumn : entityTable.selectColumns()) {
            String column = entityColumn.column();
            //去掉可能存在的分隔符，例如：`order`
            Matcher matcher = EntityTable.DELIMITER.matcher(column);
            if (matcher.find()) {
                column = matcher.group(1);
            }
            ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.property(), column, entityColumn.javaType());
            if (entityColumn.jdbcType() != null && entityColumn.jdbcType() != JdbcType.UNDEFINED) {
                builder.jdbcType(entityColumn.jdbcType());
            }
            if (entityColumn.typeHandler() != null && entityColumn.typeHandler() != UnknownTypeHandler.class) {
                try {
                    builder.typeHandler(entityTable.getTypeHandlerInstance(entityColumn.javaType(), entityColumn.typeHandler()));
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
        String resultMapId = entityTable.entityClass().getName() + "." + EntityTable.RESULT_MAP_NAME;
        ResultMap.Builder builder = new ResultMap.Builder(configuration, resultMapId, entityTable.entityClass(), resultMappings, true);
        return builder.build();
    }

}
