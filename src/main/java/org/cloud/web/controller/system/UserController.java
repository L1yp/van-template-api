package org.cloud.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.cloud.model.common.PageData;
import org.cloud.model.common.ResultData;
import org.cloud.web.context.LoginUtils;
import org.cloud.web.model.DO.system.UserDO;
import org.cloud.web.model.DTO.in.EntityIdListDTO;
import org.cloud.web.model.DTO.in.system.MailVerifyCodeGetDTO;
import org.cloud.web.model.DTO.in.system.UserAddDTO;
import org.cloud.web.model.DTO.in.system.UserChangePwdDTO;
import org.cloud.web.model.DTO.in.system.UserLoginDTO;
import org.cloud.web.model.DTO.in.system.UserMailBindDTO;
import org.cloud.web.model.DTO.in.system.UserQueryPageDTO;
import org.cloud.web.model.DTO.in.system.UserRegisterDTO;
import org.cloud.web.model.DTO.in.system.UserRoleBindDTO;
import org.cloud.web.model.DTO.in.system.UserUpdateDTO;
import org.cloud.web.model.DTO.out.system.UserLoginResultDTO;
import org.cloud.web.model.DTO.out.system.UserOutputDTO;
import org.cloud.web.service.system.IUserService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "用户")
@SaCheckLogin
@Validated
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    IUserService service;

    String defaultRole = "2";

    HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    String getRequestIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getRemoteAddr();
    }

    @Operation(summary = "获取登录状态信息")
    @GetMapping("/state")
    public ResultData<UserLoginResultDTO> getLoginState() {
        UserLoginResultDTO loginResult = service.getLoginResult(LoginUtils.getLoginUserId());
        return ResultData.ok(loginResult);
    }

    @SaCheckPermission("user.role.list")
    @Operation(summary = "获取用户绑定的角色Id列表")
    @GetMapping("/role")
    public ResultData<List<String>> getRoleList(@RequestParam @NotBlank String uid) {
        return ResultData.ok(service.getRoleIdList(uid));
    }

    @SaCheckPermission("user.dept.list")
    @Operation(summary = "获取用户绑定的部门Id列表")
    @GetMapping("/dept")
    public ResultData<List<String>> getDeptList(@RequestParam @NotBlank String uid) {
        return ResultData.ok(service.getDeptIdList(uid));
    }

    @SaCheckPermission("user.perm.list")
    @Operation(summary = "获取用户绑定的权限Key列表")
    @GetMapping("/perm")
    public ResultData<List<String>> getPermKeyList() {
        return ResultData.ok(service.getPermKeyList(LoginUtils.getLoginUserId()));
    }


    /**
     * TODO: ADMIN操作
     */
    @SaCheckPermission("user.role.bind")
    @Operation(summary = "绑定用户角色")
    @PostMapping("/bind/role")
    public ResultData<Void> bingRole(@Validated @RequestBody UserRoleBindDTO param) {
        service.bindRole(param);
        return ResultData.OK;
    }

    // 客户端接口
    @SaIgnore
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResultData<Void> register(@Validated @RequestBody UserRegisterDTO param) {
        if (StpUtil.isLogin()) {
            return ResultData.err(400, "禁止以登录状态下注册帐号, 请先退出登录");
        }
        param.setRegisterIp(getRequestIp());
        service.register(param);
        return ResultData.OK;
    }


    @SaIgnore
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResultData<UserLoginResultDTO> login(@Validated @RequestBody UserLoginDTO param) {
        param.setLoginIp(getRequestIp());
        UserLoginResultDTO resultDTO = service.login(param);
        return ResultData.ok(resultDTO);
    }

    @SaCheckPermission("user.password.change")
    @Operation(summary = "用户修改密码")
    @PostMapping("/changePwd")
    public ResultData<Void> changePwd(@Validated @RequestBody UserChangePwdDTO param) {
        service.changePwd(param);
        return ResultData.OK;
    }

    @PostMapping("/add")
    @Operation(summary = "新增用户")
    @SaCheckPermission("user.add")
    public ResultData<Void> add(@Validated @RequestBody UserAddDTO param) {
        param.setRegisterIp(getRequestIp());
        if (StringUtils.isBlank(param.getParentId())) {
            param.setParentId(null);
        }
        if (CollectionUtils.isEmpty(param.getRoleIdList())) {
            param.setRoleIdList(List.of(defaultRole)); // use config service get default role
        } else if (param.getRoleIdList().contains(defaultRole)) {
            param.getRoleIdList().add(defaultRole);
        }
        service.insert(param);
        return ResultData.OK;
    }

    @SaCheckPermission("user.delete")
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public ResultData<Void> deleteById(@PathVariable("id") String id) {
        service.deleteById(id);
        return ResultData.OK;
    }

    @PostMapping("/getEntityList")
    @Operation(summary = "批量查询用户")
    @SaCheckPermission("user.entity.list")
    public ResultData<List<UserOutputDTO>> getEntityList(@Validated @RequestBody EntityIdListDTO param) {
        List<UserOutputDTO> result = new ArrayList<>();
        for (String id : param.getIdList()) {
            UserDO model = service.getById(id);
            if (model == null) {
                continue;
            }
            result.add(model.convert());
        }
        return ResultData.ok(result);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询数据")
    @SaCheckPermission("user.page")
    public ResultData<PageData<UserOutputDTO>> page(@Validated UserQueryPageDTO param) {
        return ResultData.ok(service.page(param));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户")
    @SaCheckPermission("user.update")
    public ResultData<Void> update(@Validated @RequestBody UserUpdateDTO param) {
        if (CollectionUtils.isEmpty(param.getRoleIdList())) {
            param.setRoleIdList(List.of(defaultRole)); // use config service get default role
        } else if (param.getRoleIdList().contains(defaultRole)) {
            param.getRoleIdList().add(defaultRole);
        }
        service.update(param);
        return ResultData.OK;
    }


    @SaCheckLogin
    @Operation(summary = "获取邮箱验证码", description = "绑定邮箱前获取邮箱验证码及会话Token")
    @GetMapping("/mail/bind/getVerifyCode")
    public ResultData<String> getMailVerifyCode(@Validated MailVerifyCodeGetDTO param) {
        param.setLoginUserId(LoginUtils.getLoginUserId());
        return ResultData.ok(service.getMailVerifyCode(param));
    }

    @SaCheckLogin
    @Operation(summary = "绑定邮箱", description = "绑定邮箱")
    @PostMapping("/mail/bind")
    public ResultData<Void> bindMail(@Validated @RequestBody UserMailBindDTO param) {
        param.setLoginUserId(LoginUtils.getLoginUserId());
        service.bindMail(param);
        return ResultData.OK;
    }
}
