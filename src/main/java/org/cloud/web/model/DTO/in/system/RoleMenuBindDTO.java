package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色绑定菜单参数")
public class RoleMenuBindDTO {

    @NotNull
    @Min(1)
    @Schema(description = "角色ID")
    private String roleId;

    @NotNull
    @Size(min = 1, max = 100)
    @Schema(description = "菜单ID列表")
    private List<String> menuIds;

}