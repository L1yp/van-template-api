package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.web.model.DO.system.RolePermDO;

@Getter
@Setter
@Schema(description = "角色权限关联类型")
public class RolePermOutputDTO extends AbstractOutputDTO<RolePermDO> {

    @Schema(description = "角色ID")
    private String roleId;

    @Schema(description = "权限标识符")
    private String perm;

}