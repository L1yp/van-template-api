package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.model.enums.UserTwoFAType;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.web.model.DO.system.UserTwoFAKeyDO;

@Getter
@Setter
@Schema(description = "二次验证数据")
public class UserTwoFAKeyOutputDTO extends AbstractOutputDTO<UserTwoFAKeyDO> {

    @NotBlank
    @Schema(description = "用户ID")
    private String userId;

    @NotNull
    @BasicEnumValid(UserTwoFAType.class)
    private Integer type;

    private String secretKey;

    private Boolean bound;

}
