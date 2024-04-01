package org.cloud.web.model.DTO.in.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.common.PageDTO;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.base.BasicEnumValid;

@Getter
@Setter
@Schema(description = "用户分页列表参数")
public class UserQueryPageDTO extends PageDTO {

    @Schema(description = "关键词")
    private String keyword;

    @Schema(description = "关键父账号Id")
    private String parentId;

    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态")
    private Integer status;

}
