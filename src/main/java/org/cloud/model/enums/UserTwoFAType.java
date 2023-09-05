package org.cloud.model.enums;

import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.ElTagEnum;
import org.cloud.model.enums.base.TagAttribute;
import org.cloud.model.enums.base.TagAttribute.TagType;

public enum UserTwoFAType implements BasicEnum, ElTagEnum {
    Authenticator(1, "Authenticator", new TagAttribute(TagType.info)),
    ;


    private final int value;
    private final String description;
    private final TagAttribute attrs;

    UserTwoFAType(int value, String description, TagAttribute attrs) {
        this.value = value;
        this.description = description;
        this.attrs = attrs;
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
        return "user.2fa.type";
    }

    @Override
    public TagAttribute getTagAttribute() {
        return attrs;
    }
}
