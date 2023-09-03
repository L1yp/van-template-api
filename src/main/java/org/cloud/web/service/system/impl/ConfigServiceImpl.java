package org.cloud.web.service.system.impl;

import org.checkerframework.checker.units.qual.A;
import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.ElTagEnum;
import org.cloud.web.model.DTO.out.system.ConfigEnumOutputDTO;
import org.cloud.web.service.system.IConfigService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ConfigServiceImpl implements IConfigService {

    private String basePackage = "org.cloud.model.enums";

    public Map<String, List<ConfigEnumOutputDTO>> getBasicEnumList() {
        List<Class<?>> classList = getBasicEnumClassList();
        Map<String, List<ConfigEnumOutputDTO>> result = new LinkedHashMap<>(classList.size() * 2);

        for (Class<?> clazz : classList) {
            Object[] enumConstants = clazz.getEnumConstants();
            List<ConfigEnumOutputDTO> rows = new ArrayList<>();
            String key = null;
            for (Object enumConstant : enumConstants) {
                BasicEnum basicEnum = (BasicEnum) enumConstant;
                key = basicEnum.getKey();
                var item = new ConfigEnumOutputDTO();
                item.setValue(basicEnum.getValue());
                item.setDescription(basicEnum.description());
                if (basicEnum instanceof ElTagEnum tagEnum) {
                    item.setAttribute(tagEnum.getTagAttribute());
                }
                rows.add(item);
            }
            result.put(key, rows);
        }

        return result;
    }


    public List<Class<?>> getBasicEnumClassList() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(BasicEnum.class)); // 可以添加过滤条件
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);

        List<Class<?>> result = new ArrayList<>();

        for (BeanDefinition candidateComponent : candidateComponents) {
            String beanClassName = candidateComponent.getBeanClassName();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(beanClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (!BasicEnum.class.isAssignableFrom(clazz) || !clazz.isEnum()) {
                continue;
            }
            result.add(clazz);

        }

        return result;
    }

}
