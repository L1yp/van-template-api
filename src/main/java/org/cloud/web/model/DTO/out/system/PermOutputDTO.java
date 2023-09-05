package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.web.model.DO.system.PermDO;

@Getter
@Setter
@Schema(description = "权限类型")
public class PermOutputDTO extends AbstractOutputDTO<PermDO> {

    @Schema(description = "父级权限ID")
    private String pid;

    /**
     * 若非最底层则不需要设置
     */
    @Schema(description = "权限标识符")
    private String permKey;

    @Schema(description = "权限说明")
    private String name;

    @Schema(description = "排序")
    private Integer orderNo;

}
