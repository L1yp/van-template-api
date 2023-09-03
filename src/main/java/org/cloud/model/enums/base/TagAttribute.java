package org.cloud.model.enums.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "标签属性列表")
@JsonInclude(Include.NON_NULL)
public class TagAttribute {

    @Schema(description = "标签类型")
    private TagType type;

    @Schema(description = "是否圆形标签")
    private Boolean round;

    @Schema(description = "是否禁用渐变动画")
    private Boolean disableTransitions;

    @Schema(description = "是否有边框描边")
    private Boolean hit;

    @Schema(description = "是否圆形标签")
    private TagEffect effect;

    public TagAttribute(TagType type) {
        this.type = type;
    }

    public enum TagEffect {
        dark, light, plain,
        ;
    }

    public enum TagType {
        success,
        info,
        warning,
        danger,
        ;
    }

}
