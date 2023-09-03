package org.cloud.model.enums;

import org.cloud.model.enums.base.BasicEnum;

public enum UserDeptType implements BasicEnum {

    PRIMARY(1, "主部门"),
    SECONDARY(2, "兼职部门")
    ;


    private final int value;

    private final String description;

    UserDeptType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String getKey() {
        return "user.dept.type";
    }
}
