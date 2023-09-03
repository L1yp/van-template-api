package org.cloud.model.enums;

import org.cloud.model.enums.base.TagAttribute;
import org.cloud.model.enums.base.TagAttribute.TagType;
import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.ElTagEnum;

/**
 * 通用状态枚举
 */
public enum CommonStatus implements ElTagEnum, BasicEnum {
    ENABLE(1, "启用", new TagAttribute(null)),
    DISABLE(0, "禁用", new TagAttribute(TagType.danger)),
    ;


    private final int value;
    private final String description;
    private final TagAttribute attrs;



    CommonStatus(int value, String description, TagAttribute attrs) {
        this.value = value;
        this.description = description;
        this.attrs = attrs;
    }

    @Override
    public String getKey() {
        return "common.status";
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
    public TagAttribute getTagAttribute() {
        return attrs;
    }
}
