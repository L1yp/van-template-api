package org.cloud.model.enums;

import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.ElTagEnum;
import org.cloud.model.enums.base.TagAttribute;
import org.cloud.model.enums.base.TagAttribute.TagType;

/**
 * 是否枚举
 */
public enum YesNo implements ElTagEnum, BasicEnum {
    YES(1, "是", new TagAttribute(null)),
    NO(0, "否", new TagAttribute(TagType.danger)),
    ;


    private final int value;
    private final String description;
    private final TagAttribute attrs;



    YesNo(int value, String description, TagAttribute attrs) {
        this.value = value;
        this.description = description;
        this.attrs = attrs;
    }

    @Override
    public String getKey() {
        return "yes.no";
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
