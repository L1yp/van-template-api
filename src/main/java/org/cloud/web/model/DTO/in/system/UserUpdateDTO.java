package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.web.model.DO.system.UserDO;

import java.util.List;

@Getter
@Setter
@Schema(description = "更新用户类型")
public class UserUpdateDTO extends AbstractUpdateDTO<UserDO> {

    @NotBlank
    @Schema(description = "昵称", example = "韩信")
    private String nickname;

    @Schema(description = "头像", example = "https://q4.qlogo.cn/g?b=qq&nk=942664114&s=0")
    private String avatar;

    @Schema(description = "部门ID")
    private String deptId;

    @Schema(description = "兼职部门ID列表")
    private List<String> deptIdList;

    @Schema(description = "角色ID列表")
    private List<String> roleIdList;

    @NotNull
    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态", example = "1")
    private Integer status;

}
