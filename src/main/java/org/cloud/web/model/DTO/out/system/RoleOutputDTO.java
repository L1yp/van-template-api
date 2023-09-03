package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractWithUpdateOutputDTO;
import org.cloud.web.model.DO.system.RoleDO;

@Getter
@Setter
@Schema(description = "角色类型")
public class RoleOutputDTO extends AbstractWithUpdateOutputDTO<RoleDO> {

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色分类")
    private String category;

    @Schema(description = "角色序号")
    private Integer orderNo;

    @Schema(description = "角色状态")
    private Integer status;

}
