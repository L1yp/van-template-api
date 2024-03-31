package org.cloud.web.model.DTO.in.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.Converter;
import org.cloud.web.model.DO.system.UserDO;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "用户注册类型")
public class UserRegisterDTO implements Converter<UserDO> {

    @Pattern(regexp = "^[a-zA-Z_$][\\w$]*$", message = "用户名必须字母或下划线开头")
    @Schema(description = "用户名", example = "Lyp")
    private String username;

    @Length(min = 32, max = 32)
    @Schema(description = "用户密码的MD5")
    private String password;

    @NotBlank
    @Schema(description = "昵称", example = "韩信")
    private String nickname;

    @JsonIgnore
    @Schema(description = "注册IP", example = "127.0.0.1", hidden = true)
    private String registerIp;


    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "邮件验证码")
    private String code;

}
