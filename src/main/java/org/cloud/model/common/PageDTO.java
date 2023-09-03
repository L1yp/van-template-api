package org.cloud.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "分页参数")
public class PageDTO {

    @Schema(description = "页码", example = "1")
    private int pageIdx = 1;

    @Schema(description = "每页条数", example = "20")
    private int pageSize = 20;


    /**
     * SQL Offset
     */
    @JsonIgnore
    @Parameter(hidden = true)
    public int getOffset() {
        return (pageIdx - 1) * pageSize;
    }


}
