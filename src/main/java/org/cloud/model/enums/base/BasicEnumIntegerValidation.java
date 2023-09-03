package org.cloud.model.enums.base;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashMap;
import java.util.Map;

public class BasicEnumIntegerValidation implements ConstraintValidator<BasicEnumValid, Integer> {

    private final Map<Integer, BasicEnum> valMap = new HashMap<>();

    @Override
    public void initialize(BasicEnumValid constraintAnnotation) {
        Class<? extends BasicEnum> value = constraintAnnotation.value();
        if (!value.isEnum()) {
            throw new UnsupportedOperationException("value必须是BasicEnum的子类, 并且必须是枚举");
        }
        BasicEnum[] constants = value.getEnumConstants();

        for (BasicEnum constant : constants) {
            Integer key = constant.getValue();
            valMap.put(key, constant);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || valMap.containsKey(value);
    }
}
