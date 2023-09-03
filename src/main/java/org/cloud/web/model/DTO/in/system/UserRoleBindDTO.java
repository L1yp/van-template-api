package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "用户角色绑定结构")
public class UserRoleBindDTO {

    @NotBlank
    @Schema(description = "用户ID")
    private String userId;


    @Schema(description = "角色ID列表")
    private List<String> roleIdList;

}
