package org.cloud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 输出基类(包含更新人、更新时间)
 * @param <DO> 可直接转换的DO类型
 */
@Getter
@Setter
public abstract class AbstractWithUpdateOutputDTO<DO> extends AbstractOutputDTO<DO> {

    @Schema(description = "更新人ID", requiredMode = RequiredMode.REQUIRED)
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", requiredMode = RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}
