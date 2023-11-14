package org.cloud.mybatis;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Getter
@Slf4j
@Component
public class MybatisMapperBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final List<Class<?>> mapperClazzList = new ArrayList<>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("beanFactory: {}", beanFactory);
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (!beanDefinitionName.endsWith("Mapper")) {
                continue;
            }
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (!"org.mybatis.spring.mapper.MapperFactoryBean".equals(beanClassName)) {
                continue;
            }

            List<ValueHolder> genericArgumentValues = beanDefinition.getConstructorArgumentValues().getGenericArgumentValues();
            if (CollectionUtils.isEmpty(genericArgumentValues)) {
                continue;
            }

            ValueHolder valueHolder = genericArgumentValues.get(0);
            Object value = valueHolder.getValue();
            if (value instanceof String mapperInterface) {
                try {
                    Class<?> mapperClazz = Resources.classForName(mapperInterface);
                    mapperClazzList.add(mapperClazz);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException { }



}
