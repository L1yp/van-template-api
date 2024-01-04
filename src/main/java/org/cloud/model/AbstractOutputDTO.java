package org.cloud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 输出基类
 * @param <DO> 可直接转换的DO类型
 */
@Getter
@Setter
public abstract class AbstractOutputDTO<DO> implements Converter<DO> {

    @Schema(description = "ID", requiredMode = RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "创建者ID", requiredMode = RequiredMode.REQUIRED)
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", requiredMode = RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
