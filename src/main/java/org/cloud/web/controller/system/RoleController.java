package org.cloud.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.cloud.model.common.ResultData;
import org.cloud.web.context.LoginUtils;
import org.cloud.web.model.DTO.in.system.RoleAddDTO;
import org.cloud.web.model.DTO.in.system.RoleMenuBindDTO;
import org.cloud.web.model.DTO.in.system.RolePermBindDTO;
import org.cloud.web.model.DTO.in.system.RoleUpdateDTO;
import org.cloud.web.model.DTO.out.system.PermOutputDTO;
import org.cloud.web.model.DTO.out.system.RoleOutputDTO;
import org.cloud.web.service.system.IRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "角色")
@SaCheckLogin
@Validated
@RequestMapping("/role")
@RestController
public class RoleController {

    @Resource
    IRoleService roleService;

    @SaCheckPermission("role.add")
    @PostMapping("/add")
    @Operation(summary = "新增角色")
    public ResultData<Void> add(@RequestBody @Validated RoleAddDTO param) {
        roleService.insertSelective(param);
        return ResultData.OK;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @SaCheckPermission("role.delete")
    public ResultData<Void> deleteById(@Validated @PathVariable("id") @NotBlank String id) {
        roleService.deleteById(id);
        return ResultData.OK;
    }

    @GetMapping("/list")
    @Operation(summary = "查询角色列表")
    @SaCheckPermission("role.list")
    public ResultData<List<RoleOutputDTO>> list() {
        return ResultData.ok(roleService.list(null));
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色")
    @SaCheckPermission("role.update")
    public ResultData<Void> update(@RequestBody @Validated RoleUpdateDTO param) {
        roleService.updateByPrimaryKeySelective(param);
        return ResultData.OK;
    }

    @SaCheckPermission("role.menu.bind")
    @Operation(summary = "绑定角色的菜单列表")
    @PostMapping("/menu/bind")
    public ResultData<Void> bindMenu(@Validated @RequestBody RoleMenuBindDTO param) {
        roleService.bindMenu(LoginUtils.getLoginUserId(), param);
        return ResultData.OK;
    }

    @SaCheckPermission("role.menu.list")
    @Operation(summary = "查询角色绑定的菜单ID列表")
    @GetMapping("/menu")
    public ResultData<List<String>> menuBound(@NotBlank String roleId) {
        return ResultData.ok(roleService.listMenuIdByRoleId(roleId));
    }

    @SaCheckPermission("role.perm.bind")
    @Operation(summary = "绑定角色的权限列表")
    @PostMapping("/perm/bind")
    public ResultData<Void> bindPerm(@Validated @RequestBody RolePermBindDTO param) {
        roleService.bindPerm(LoginUtils.getLoginUserId(), param);
        return ResultData.OK;
    }

    @SaCheckPermission("role.perm.list")
    @Operation(summary = "查询角色绑定的权限列表")
    @GetMapping("/perm")
    public ResultData<List<PermOutputDTO>> permBound(@NotBlank String roleId) {
        return ResultData.ok(roleService.listPermByRoleId(roleId));
    }

}
