package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractModel;
import org.cloud.web.model.DTO.out.system.PermOutputDTO;

@Getter
@Setter
@Table(value = "sys_perm", autoResultMap = true)
public class PermDO extends AbstractModel<PermOutputDTO> {

    @Column(jdbcType = JdbcType.BIGINT)
    private String pid;

    /**
     * 权限标识符
     */
    private String permKey;

    /**
     * 权限说明
     */
    private String name;

    private Integer orderNo;

}
