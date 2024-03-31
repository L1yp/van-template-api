package org.cloud.web.model.DTO.in.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "绑定邮箱参数")
public class UserMailBindDTO {

    @NotBlank
    @Schema(description = "邮件地址")
    private String mail;

    @NotBlank
    @Schema(description = "邮件验证码")
    private String code;

    @JsonIgnore
    @Schema(hidden = true)
    private String loginUserId;

}
