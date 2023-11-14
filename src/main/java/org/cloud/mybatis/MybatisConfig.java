package org.cloud.mybatis;

import io.mybatis.provider.Entity.Table;
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
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    MybatisMapperBeanDefinitionRegistryPostProcessor mybatisMapperBeanDefinitionRegistryPostProcessor;

    @Override
    public void customize(Configuration configuration) {
        this.configuration = configuration;
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        List<Class<?>> classList = configService.getBasicEnumClassList();
        classList.forEach(clazz -> typeHandlerRegistry.register(clazz, BasicEnumTypeHandler.class));

        // 自动构建resultMap
        List<Class<?>> mapperClazzList = mybatisMapperBeanDefinitionRegistryPostProcessor.getMapperClazzList();
        Set<Class<?>> resultTypeList = new HashSet<>();
        mapperClazzList.forEach(clazz -> getMapperResultTypeList(clazz, resultTypeList));
        autoBuildResultMap(configuration, resultTypeList);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        resetTypeHandlerInResultMapping();
    }

    /**
     * 重置jacksonTypeHandler的fieldType，以支持泛型字段
     */
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

    public Class<?> getMapperDOType(Class<?> mapperClazz) {
        Type[] genericInterfaces = mapperClazz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getRawType() == Mapper.class || parameterizedType.getRawType() == io.mybatis.mapper.Mapper.class) {
                    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    public void getMapperResultTypeList(Class<?> mapperClazz, Set<Class<?>> candidateBuildResultMapTypeList) {
        // 获取mapper的第一个泛型参数类型
        Class<?> mapperDOType = getMapperDOType(mapperClazz);
        if (mapperDOType == null) {
            return;
        }
        Method[] methods = mapperClazz.getMethods();
        for (Method declaredMethod : methods) {
            // 获取方法所在定义类
            Class<?> declaringClass = declaredMethod.getDeclaringClass();
            // 查询mapper中声明的对象中的resultMap
            if (declaringClass != mapperClazz) {
                continue;
            }
            // 获取方法的泛型返回值
            Type genericReturnType = declaredMethod.getGenericReturnType();
            // 若返回值是泛型
            if (genericReturnType instanceof ParameterizedType parameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                // 处理泛型参数有且仅有一个的返回值类型
                if (actualTypeArguments == null || actualTypeArguments.length != 1) {
                    continue;
                }
                Type actualTypeArgument = actualTypeArguments[0];
                if (actualTypeArgument == mapperDOType) {
                    continue;
                }
                if (actualTypeArgument instanceof Class<?> genericType) {
                    if (genericType.isAnnotationPresent(Table.class)) {
                        candidateBuildResultMapTypeList.add(genericType);
                    }

                }
            }
            // 返回值是直接类型
            else if (genericReturnType instanceof Class<?> returnClazz) {
                // 返回值是DO类型，无需重复生成result map
                if (returnClazz == mapperDOType) {
                    continue;
                }
                if (returnClazz.isAnnotationPresent(Table.class)) {
                    candidateBuildResultMapTypeList.add(returnClazz);
                }
            }
        }
    }

    public void autoBuildResultMap(Configuration configuration, Set<Class<?>> resultTypeList) {
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
