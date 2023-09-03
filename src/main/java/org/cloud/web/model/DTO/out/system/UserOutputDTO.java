package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractWithUpdateOutputDTO;
import org.cloud.web.model.DO.system.UserDO;

@Getter
@Setter
@Schema(description = "用户类型")
public class UserOutputDTO extends AbstractWithUpdateOutputDTO<UserDO> {

    @Schema(description = "用户名", requiredMode = RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "昵称", requiredMode = RequiredMode.REQUIRED)
    private String nickname;

    @Schema(description = "昵称拼音", requiredMode = RequiredMode.REQUIRED)
    private String nicknamePinyin;

    @Schema(description = "部门ID")
    private String deptId;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "注册IP", requiredMode = RequiredMode.REQUIRED)
    private String registerIp;

    @Schema(description = "状态", requiredMode = RequiredMode.REQUIRED)
    private Integer status;

}
