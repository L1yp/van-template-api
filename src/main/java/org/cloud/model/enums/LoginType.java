package org.cloud.model.enums;

import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.ElTagEnum;
import org.cloud.model.enums.base.TagAttribute;
import org.cloud.model.enums.base.TagAttribute.TagType;

public enum LoginType implements BasicEnum, ElTagEnum {
    PWD(1, "密码", new TagAttribute(TagType.success)),
    REGISTER(2, "注册", new TagAttribute(TagType.success)),
    ;

    private final int value;

    private final String description;
    private final TagAttribute attribute;

    LoginType(int value, String description, TagAttribute attribute) {
        this.value = value;
        this.description = description;
        this.attribute = attribute;
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
        return "login.type";
    }

    @Override
    public TagAttribute getTagAttribute() {
        return attribute;
    }
}
