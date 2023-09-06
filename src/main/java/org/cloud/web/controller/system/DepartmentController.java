package org.cloud.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.cloud.model.common.ResultData;
import org.cloud.web.model.DTO.in.system.DepartmentAddDTO;
import org.cloud.web.model.DTO.in.system.DepartmentUpdateDTO;
import org.cloud.web.model.DTO.out.system.DepartmentOutputDTO;
import org.cloud.web.service.system.IDepartmentService;
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

@Tag(name = "部门")
@Validated
@SaCheckLogin
@RequestMapping("/dept")
@RestController
public class DepartmentController {


    @Resource
    IDepartmentService departmentService;

    @SaCheckPermission("dept.add")
    @Operation(summary = "新增部门")
    @PostMapping("/add")
    public ResultData<Void> add(@RequestBody @Validated DepartmentAddDTO param) {
        departmentService.insert(param);
        return ResultData.OK;
    }

    @SaCheckPermission("dept.delete")
    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    public ResultData<Void> deleteById(@PathVariable("id") String id) {
        departmentService.deleteById(id);
        return ResultData.OK;
    }

    @SaCheckPermission("dept.list")
    @GetMapping("/list")
    @Operation(summary = "查询全部部门")
    public ResultData<List<DepartmentOutputDTO>> list() {
        return ResultData.ok(departmentService.list());
    }


    @PutMapping("/update")
    @Operation(summary = "更新部门")
    @SaCheckPermission("dept.update")
    public ResultData<Void> update(@RequestBody @Validated DepartmentUpdateDTO param) {
        departmentService.updateByPrimaryKeySelective(param);
        return ResultData.OK;
    }


}
