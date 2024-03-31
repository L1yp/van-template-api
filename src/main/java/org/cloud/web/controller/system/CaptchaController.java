package org.cloud.web.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.cloud.model.common.ResultData;
import org.cloud.util.TokenUtil;
import org.cloud.web.model.DTO.out.system.CaptchaImageDTO;
import org.cloud.web.service.system.ICaptchaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/captcha")
@Tag(name = "验证码")
@Validated
public class CaptchaController {

    @Resource
    ICaptchaService captchaService;


    @GetMapping(value = "/image")
    @Operation(summary = "获取验证码图片流", description = "获取验证码接口")
    public ResultData<CaptchaImageDTO> getCaptchaImage() {
        var param = new CaptchaImageDTO(TokenUtil.genToken());
        captchaService.generateCaptchaImage(param);
        return ResultData.ok(param);
    }


}
