package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.web.model.DO.system.UserRoleDO;

@Getter
@Setter
@Schema(description = "用户角色关联类型")
public class UserRoleOutputDTO extends AbstractOutputDTO<UserRoleDO> {

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "角色ID")
    private String roleId;

}
