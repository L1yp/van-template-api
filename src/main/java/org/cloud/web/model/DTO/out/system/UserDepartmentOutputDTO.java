package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.web.model.DO.system.UserDepartmentDO;

@Getter
@Setter
@Schema(description = "用户兼职部门关联信息")
public class UserDepartmentOutputDTO extends AbstractOutputDTO<UserDepartmentDO> {

    @Schema(description = "用户ID", requiredMode = RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "部门ID", requiredMode = RequiredMode.REQUIRED)
    private String departmentId;

}
