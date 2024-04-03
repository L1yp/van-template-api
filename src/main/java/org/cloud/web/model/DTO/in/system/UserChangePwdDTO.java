package org.cloud.web.model.DTO.in.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "修改密码参数")
public class UserChangePwdDTO {

    @Length(min = 32, max = 32)
    @NotBlank
    @Schema(description = "旧密码的MD5")
    private String oldPassword;

    @Length(min = 32, max = 32)
    @NotBlank
    @Schema(description = "新密码的MD5")
    private String newPassword;

    @JsonIgnore
    @Schema(hidden = true)
    private String loginUserId;

}
