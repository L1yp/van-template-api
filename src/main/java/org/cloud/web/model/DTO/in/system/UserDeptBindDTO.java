package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "用户部门绑定结构")
public class UserDeptBindDTO {

    @NotBlank
    @Schema(description = "用户ID")
    private String userId;


    @Schema(description = "主部门ID")
    private String deptId;


    @Schema(description = "部门ID列表")
    private List<String> deptIdList;

}
