package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.enums.base.TagAttribute;

@Getter
@Setter
@Schema(description = "枚举信息")
public class ConfigEnumOutputDTO {

    @Schema(description = "枚举值")
    private Integer value;

    @Schema(description = "枚举描述")
    private String description;

    @Schema(description = "标签属性")
    private TagAttribute attribute;

}
