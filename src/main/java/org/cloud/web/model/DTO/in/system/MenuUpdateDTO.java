package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.model.enums.CommonStatus;
import org.cloud.web.model.DO.system.MenuDO;
import org.cloud.web.model.DO.system.MenuDO.MenuMeta;

@Getter
@Setter
@Schema(description = "更新角色类型")
public class MenuUpdateDTO extends AbstractUpdateDTO<MenuDO> {

    @NotBlank
    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "附加数据")
    private MenuMeta meta;

    @NotNull
    @Schema(description = "序号")
    private Integer orderNo;

    @NotNull
    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
