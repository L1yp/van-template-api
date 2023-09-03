package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractWithUpdateModel;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.MenuType;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.web.model.DO.system.MenuDO;
import org.cloud.web.model.DO.system.MenuDO.MenuMeta;

@Getter
@Setter
@Schema(description = "菜单结构")
public class MenuOutputDTO extends AbstractWithUpdateModel<MenuDO> {

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "父级菜单ID")
    private String pid;

    @Schema(description = "菜单类型")
    private MenuType type;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "附加数据")
    private MenuMeta meta;

    @Schema(description = "序号")
    private Integer orderNo;

    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;


}
