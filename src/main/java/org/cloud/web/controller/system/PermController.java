package org.cloud.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.cloud.model.common.ResultData;
import org.cloud.web.model.DO.system.PermDO;
import org.cloud.web.model.DTO.in.EntityIdListDTO;
import org.cloud.web.model.DTO.in.system.PermAddDTO;
import org.cloud.web.model.DTO.in.system.PermUpdateDTO;
import org.cloud.web.model.DTO.out.system.PermOutputDTO;
import org.cloud.web.service.system.IPermService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "权限")
@Validated
@SaCheckLogin
@RequestMapping("/perm")
@RestController
public class PermController {

    @Resource
    IPermService permService;

    @SaCheckPermission("perm.add")
    @Operation(summary = "新增权限")
    @PostMapping("/add")
    public ResultData<Void> add(@RequestBody @Validated PermAddDTO param) {
        if (StringUtils.isBlank(param.getPid())) {
            param.setPid(null);
        }
        permService.add(param);
        return ResultData.OK;
    }

    @SaCheckPermission("perm.delete")
    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限")
    public ResultData<Void> deleteById(@Validated @PathVariable("id") @NotBlank String id) {
        permService.deleteById(id);
        return ResultData.OK;
    }

    @SaCheckPermission("perm.list")
    @GetMapping("/list")
    @Operation(summary = "查询权限列表")
    public ResultData<List<PermOutputDTO>> list() {
        return ResultData.ok(permService.list(null));
    }

    @GetMapping("/getEntityList")
    @Operation(summary = "批量查询权限")
    @SaCheckPermission("perm.entity.list")
    public ResultData<List<PermOutputDTO>> getEntityList(@RequestBody @Validated EntityIdListDTO param) {
        List<PermOutputDTO> result = new ArrayList<>();
        for (String id : param.getIdList()) {
            PermDO model = permService.getById(id);
            if (model == null) {
                continue;
            }
            result.add(model.convert());
        }
        return ResultData.ok(result);
    }

    @PutMapping("/update")
    @Operation(summary = "更新权限")
    @SaCheckPermission("perm.update")
    public ResultData<Void> update(@RequestBody @Validated PermUpdateDTO param) {
        permService.update(param);
        return ResultData.OK;
    }
}
