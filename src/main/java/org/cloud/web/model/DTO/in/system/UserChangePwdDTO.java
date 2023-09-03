package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "修改密码参数")
public class UserChangePwdDTO {

    @NotBlank
    @Schema(description = "用户名")
    private String username;

    @Length(min = 32, max = 32)
    @NotBlank
    @Schema(description = "新密码的MD5")
    private String newPassword;

    @NotNull
    @Schema(description = "验证码Token")
    private String captchaToken;

    @NotNull
    @Schema(description = "图形验证码")
    private String captchaCode;
}
