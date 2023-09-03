package org.cloud.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.BasicEnumValid;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author valarchie
 * SpringDoc API文档相关配置
 */
@Configuration
@OpenAPIDefinition
public class SpringDocConfig {

    @Value("${server.port:8080}")
    String port;

    @Bean
    public PropertyCustomizer propertyCustomizer() {
        return (property, type) -> {
            if ("date-time".equals(property.getFormat())) {
                // 重置format, 否则生成的ts类型是Date
                property.setFormat(null);
            }
            Annotation[] ctxAnnotations = type.getCtxAnnotations();
            if (ctxAnnotations == null) {
                return property;
            }
            for (Annotation ctxAnnotation : ctxAnnotations) {
                if (ctxAnnotation.annotationType().equals(BasicEnumValid.class)) {
                    Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(ctxAnnotation);
                    Object value = attributes.get("value");
                    if (value instanceof Class<?> clazz) {
                        if (BasicEnum.class.isAssignableFrom(clazz) && clazz.isEnum()) {
                            Object[] constants = clazz.getEnumConstants();

                            String description = Arrays.stream(constants).map(it -> (BasicEnum) it).map(it -> it.getValue() + ":" + it.description()).collect(Collectors.joining("<br>"));
                            String newDescription = property.getDescription() + "<br>" + description;
                            property.setDescription(newDescription);

                            property.setEnum(List.of(constants));
                        }
                    }
                }
            }
            return property;
        };
    }

    @Bean
    public OpenAPI publicAPI() {
        return new OpenAPI()
            .info(new Info().title("数据后台管理系统")
                .description("数据查重 API 文档")
                .version("v0.0.1")
            )
            .externalDocs(new ExternalDocumentation()
                .description("数据后台管理系统接口文档")
                .url("http://localhost:" + port + "/swagger-ui/index.html"));
    }


}
