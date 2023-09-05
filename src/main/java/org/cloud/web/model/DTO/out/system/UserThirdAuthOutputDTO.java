package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.UserThirdAuthType;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.web.model.DO.system.UserThirdAuthDO;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "第三方登录数据")
public class UserThirdAuthOutputDTO extends AbstractOutputDTO<UserThirdAuthDO> {

    @Schema(description = "用户ID", requiredMode = RequiredMode.REQUIRED)
    private String userId;

    @BasicEnumValid(UserThirdAuthType.class)
    @Schema(description = "第三方类型", requiredMode = RequiredMode.REQUIRED)
    private Integer type;

    @Schema(description = "openId", requiredMode = RequiredMode.REQUIRED)
    private String openId;

    @Schema(description = "微信unionId")
    private String unionId;

    @Schema(description = "访问Token")
    private String accessToken;

    private String avatarUrl;

    private String nickname;

    @Schema(description = "上次登录IP", requiredMode = RequiredMode.REQUIRED)
    private String lastLoginIp;

    @Schema(description = "上次登录事件", requiredMode = RequiredMode.REQUIRED)
    private LocalDateTime lastLoginTime;

    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态", requiredMode = RequiredMode.REQUIRED)
    private Integer status;

}
