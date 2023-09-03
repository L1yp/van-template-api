package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.web.model.DO.system.RoleMenuDO;

@Getter
@Setter
@Schema(description = "角色菜单关联类型")
public class RoleMenuDTO extends AbstractOutputDTO<RoleMenuDO> {

    @Schema(description = "角色ID")
    private String roleId;

    @Schema(description = "菜单ID")
    private String menuId;

}
