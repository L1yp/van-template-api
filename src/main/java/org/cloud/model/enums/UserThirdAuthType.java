package org.cloud.model.enums;

import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.ElTagEnum;
import org.cloud.model.enums.base.TagAttribute;
import org.cloud.model.enums.base.TagAttribute.TagType;

public enum UserThirdAuthType implements ElTagEnum, BasicEnum {
    QQ(1, "QQ", new TagAttribute(TagType.info)),
    WECHAT(2, "微信", new TagAttribute(TagType.info)),
    WEIBO(3, "微博", new TagAttribute(TagType.info)),
    ;


    private final int value;
    private final String description;
    private final TagAttribute attrs;

    UserThirdAuthType(int value, String description, TagAttribute attrs) {
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
        return "user.third.auth.type";
    }

    @Override
    public TagAttribute getTagAttribute() {
        return attrs;
    }
}
