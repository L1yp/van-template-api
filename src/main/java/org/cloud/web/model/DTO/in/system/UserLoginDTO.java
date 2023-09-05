package org.cloud.web.model.DTO.in.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(name = "用户登录参数类型")
public class UserLoginDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z_$][\\w$]*$", message = "key必须字母或下划线开头")
    @Schema(description = "用户名")
    private String username;

    @Length(min = 32, max = 32)
    @NotBlank
    @Schema(description = "密码MD5")
    private String password;

    @Schema(description = "验证码Token")
    private String captchaToken;

    @Schema(description = "图形验证码")
    private String captchaCode;

    @Schema(description = "Google Auth 验证码")
    private String twoFACode;

    @JsonIgnore
    @Schema(hidden = true)
    private String loginIp;

}
