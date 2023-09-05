package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.web.model.DO.system.RoleDO;

@Getter
@Setter
@Schema(description = "更新角色类型")
public class RoleUpdateDTO extends AbstractUpdateDTO<RoleDO> {

    @NotBlank
    @Schema(description = "角色名称")
    private String name;

    @NotBlank
    @Schema(description = "角色分类")
    private String category;

    @NotNull
    @Schema(description = "排序序号")
    private Integer orderNo;

    @NotNull
    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态")
    private Integer status;

}
