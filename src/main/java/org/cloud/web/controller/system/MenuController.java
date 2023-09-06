package org.cloud.web.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.cloud.model.common.ResultData;
import org.cloud.web.model.DO.system.MenuDO;
import org.cloud.web.model.DTO.in.EntityIdListDTO;
import org.cloud.web.model.DTO.in.system.MenuAddDTO;
import org.cloud.web.model.DTO.in.system.MenuUpdateDTO;
import org.cloud.web.model.DTO.out.system.MenuOutputDTO;
import org.cloud.web.service.system.IMenuService;
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

@Tag(name = "菜单")
@Validated
@SaCheckLogin
@RequestMapping("/menu")
@RestController
public class MenuController {

    @Resource
    IMenuService menuService;

    @SaCheckPermission("menu.add")
    @Operation(summary = "新增菜单")
    @PostMapping("/add")
    public ResultData<Void> add(@RequestBody @Validated MenuAddDTO param) {
        if (StringUtils.isBlank(param.getPid())) {
            param.setPid(null);
        }
        menuService.insertSelective(param);
        return ResultData.OK;
    }

    @SaCheckPermission("menu.delete")
    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public ResultData<Void> deleteById(@PathVariable("id") String id) {
        menuService.deleteById(id);
        return ResultData.OK;
    }

    @SaCheckPermission("menu.list")
    @GetMapping("/list")
    @Operation(summary = "查询全部菜单")
    public ResultData<List<MenuOutputDTO>> list() {
        return ResultData.ok(menuService.list(null));
    }


    @PutMapping("/update")
    @Operation(summary = "更新菜单")
    @SaCheckPermission("menu.update")
    public ResultData<Void> update(@RequestBody @Validated MenuUpdateDTO param) {
        menuService.updateByPrimaryKeySelective(param);
        return ResultData.OK;
    }

    @Operation(summary = "批量查询菜单")
    @SaCheckPermission("menu.entity.list")
    @PostMapping("/getEntityList")
    public ResultData<List<MenuOutputDTO>> getEntityList(@RequestBody @Validated EntityIdListDTO param) {
        List<MenuOutputDTO> result = new ArrayList<>();
        for (String id : param.getIdList()) {
            MenuDO model = menuService.getById(id);
            if (model == null) {
                continue;
            }
            result.add(model.convert());
        }
        return ResultData.ok(result);
    }
}
