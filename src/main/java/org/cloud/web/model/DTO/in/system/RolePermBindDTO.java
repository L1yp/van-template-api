package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "绑定权限入参")
public class RolePermBindDTO {

    @Schema(description = "角色ID")
    private String roleId;

    @Schema(description = "权限标识符列表")
    private List<String> permKeyList;

}
