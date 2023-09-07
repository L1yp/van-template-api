package org.cloud.web.model.DTO.in.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "发送邮件验证码")
public class MailVerifyCodeGetDTO {

    @NotBlank
    @Schema(description = "电子邮箱地址")
    private String mail;

    @JsonIgnore
    @Schema(hidden = true)
    private String loginUserId;

}
