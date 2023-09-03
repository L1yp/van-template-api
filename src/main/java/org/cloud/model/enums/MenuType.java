package org.cloud.model.enums;

import org.cloud.model.enums.base.BasicEnum;
import org.cloud.model.enums.base.ElTagEnum;
import org.cloud.model.enums.base.TagAttribute;
import org.cloud.model.enums.base.TagAttribute.TagType;

public enum MenuType implements ElTagEnum, BasicEnum {
    FOLDER(1, "文件夹", new TagAttribute(TagType.info)),
    TAB(2, "标签页", new TagAttribute(TagType.info)),
    PAGE(3, "页面", new TagAttribute(TagType.info)),
    ;

    private final int value;
    private final String description;
    private final TagAttribute attrs;


    MenuType(int value, String description, TagAttribute attrs) {
        this.value = value;
        this.description = description;
        this.attrs = attrs;
    }

    @Override
    public String getKey() {
        return "menu.type";
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