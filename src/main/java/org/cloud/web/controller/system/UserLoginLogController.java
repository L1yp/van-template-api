package org.cloud.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.cloud.model.common.PageDTO;
import org.cloud.model.common.PageData;
import org.cloud.model.common.ResultData;
import org.cloud.web.model.DTO.out.system.UserLoginLogOutputDTO;
import org.cloud.web.service.system.IUserLoginLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "用户登录日志")
@SaCheckLogin
@Validated
@RequestMapping("/user/login/log")
@RestController
public class UserLoginLogController {

    @Resource
    IUserLoginLogService service;

    @SaCheckPermission("user.login.log.page")
    @GetMapping("/page")
    @Operation(summary = "分页查询登录日志")
    public ResultData<PageData<UserLoginLogOutputDTO>> page(@Validated PageDTO param) {
        return ResultData.ok(service.page(param));
    }


}
