package org.cloud.mybatis;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
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

@Slf4j
@org.springframework.context.annotation.Configuration
public class MybatisConfig implements ConfigurationCustomizer, ApplicationListener<ContextRefreshedEvent> {

    private final ConcurrentHashMap<Type, JacksonTypeHandler> handlerMap = new ConcurrentHashMap<>(64);

    private Configuration configuration;

    @Resource
    IConfigService configService;

    @Override
    public void customize(Configuration configuration) {
        this.configuration = configuration;
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        List<Class<?>> classList = configService.getBasicEnumClassList();
        classList.forEach(clazz -> typeHandlerRegistry.register(clazz, BasicEnumTypeHandler.class));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
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

}
