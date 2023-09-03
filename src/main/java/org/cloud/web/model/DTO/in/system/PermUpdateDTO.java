package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.web.model.DO.system.PermDO;

@Getter
@Setter
@Schema(description = "权限更新接口入参")
public class PermUpdateDTO extends AbstractUpdateDTO<PermDO> {

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "排序号")
    private Integer orderNo;

}