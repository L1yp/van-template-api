package org.cloud.model;

import io.mybatis.provider.Entity.Column;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.mybatis.typehandler.IdStringTypeHandler;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractModel<DTO> implements Converter<DTO> {

    @Column(id = true, useGeneratedKeys = true, jdbcType = JdbcType.BIGINT, typeHandler = IdStringTypeHandler.class)
    private String id;

    @Column(jdbcType = JdbcType.BIGINT)
    private String createBy;

    private LocalDateTime createTime;

}
