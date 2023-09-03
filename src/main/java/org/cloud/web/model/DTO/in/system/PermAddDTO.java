package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.Converter;
import org.cloud.web.model.DO.system.PermDO;

@Getter
@Setter
@Schema(description = "权限新增入参")
public class PermAddDTO implements Converter<PermDO> {

    @Schema(description = "父级权限ID")
    private String pid;

    @Schema(description = "权限标识符")
    private String permKey;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "排序号")
    private Integer orderNo;


}
