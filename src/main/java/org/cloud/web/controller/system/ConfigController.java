package org.cloud.web.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.cloud.model.common.ResultData;
import org.cloud.web.model.DTO.out.system.ConfigEnumOutputDTO;
import org.cloud.web.service.system.IConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "配置")
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    IConfigService configService;

    @Operation(summary = "获取枚举配置信息")
    @GetMapping("/getEnumList")
    public ResultData<Map<String, List<ConfigEnumOutputDTO>>> getEnumList() {
        return ResultData.ok(configService.getBasicEnumList());
    }


}
