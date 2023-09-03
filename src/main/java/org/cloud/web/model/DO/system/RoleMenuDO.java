package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractModel;
import org.cloud.web.model.DTO.out.system.RoleMenuDTO;

@Getter
@Setter
@Table(value = "sys_role_menu", autoResultMap = true)
public class RoleMenuDO extends AbstractModel<RoleMenuDTO> {

    @Column(jdbcType = JdbcType.BIGINT)
    private String roleId;

    @Column(jdbcType = JdbcType.BIGINT)
    private String menuId;

}
