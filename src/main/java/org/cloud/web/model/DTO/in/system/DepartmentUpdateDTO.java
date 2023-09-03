package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.web.model.DO.system.DepartmentDO;

@Getter
@Setter
@Schema(description = "更新部门参数")
public class DepartmentUpdateDTO extends AbstractUpdateDTO<DepartmentDO> {

    @Schema(description = "编号", requiredMode = RequiredMode.REQUIRED)
    private String code;
    @Schema(description = "名称", requiredMode = RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "简称")
    private String simpleName;
    @Schema(description = "职能描述")
    private String description;
    @Schema(description = "排序", requiredMode = RequiredMode.REQUIRED)
    private Integer orderNo;
    @Schema(description = "上级部门ID")
    private String pid;
    @Schema(description = "部门领导")
    private String owner;
    @Schema(description = "部门助理")
    private String assistant;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "办公地点")
    private String officeLocation;
    @Schema(description = "详细地址")
    private String address;

    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态", requiredMode = RequiredMode.REQUIRED)
    private Integer status;

}
