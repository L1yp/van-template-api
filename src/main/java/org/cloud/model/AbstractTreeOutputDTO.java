package org.cloud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.common.Tree;

import java.time.LocalDateTime;

/**
 * 树形输出基类
 * @param <DO> 可直接转换的DO类型
 * @param <THIS> 当前子类型
 */
@Getter
@Setter
public abstract class AbstractTreeOutputDTO<DO, THIS extends AbstractTreeOutputDTO<DO, THIS>> extends Tree<THIS> implements Converter<DO> {

    @Schema(description = "ID", requiredMode = RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "创建者ID", requiredMode = RequiredMode.REQUIRED)
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", requiredMode = RequiredMode.REQUIRED)
    private LocalDateTime createTime;


}
