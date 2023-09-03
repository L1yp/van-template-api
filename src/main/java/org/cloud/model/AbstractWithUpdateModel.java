package org.cloud.model;

import io.mybatis.provider.Entity.Column;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractWithUpdateModel<DTO> extends AbstractModel<DTO> {

    @Column(jdbcType = JdbcType.BIGINT)
    private String updateBy;

    private LocalDateTime updateTime;

}
