package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "用户注册发送邮件参数")
public class UserRegisterMailGetDTO {

    @NotBlank
    @Schema(description = "验证码Token")
    private String captchaToken;

    @NotBlank
    @Schema(description = "图形验证码")
    private String captchaCode;

}
