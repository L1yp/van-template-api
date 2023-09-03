package org.cloud.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 树形输出基类(包含更新人、更新时间)
 * @param <DO> 可直接转换的DO类型
 * @param <THIS> 当前子类型
 */
@Getter
@Setter
public abstract class AbstractWithUpdateTreeOutputDTO<DO, THIS extends AbstractTreeOutputDTO<DO, THIS>> extends AbstractTreeOutputDTO<DO, THIS> {

    @Schema(description = "更新人ID", requiredMode = RequiredMode.REQUIRED)
    private String updateBy;

    @Schema(description = "更新时间", requiredMode = RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}
