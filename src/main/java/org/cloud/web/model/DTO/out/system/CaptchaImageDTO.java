package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "验证码数据")
public class CaptchaImageDTO {

    @NotBlank
    @Schema(description = "验证码Token")
    private String captchaToken;

    @NotBlank
    @Schema(description = "验证码数据, dataUrl")
    private String data;

    public CaptchaImageDTO(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    public CaptchaImageDTO(String captchaToken, String data) {
        this.captchaToken = captchaToken;
        this.data = data;
    }
}
