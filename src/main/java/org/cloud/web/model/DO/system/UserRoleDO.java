package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractModel;
import org.cloud.web.model.DTO.out.system.UserRoleOutputDTO;

@Getter
@Setter
@Table(value = "sys_user_role", autoResultMap = true)
public class UserRoleDO extends AbstractModel<UserRoleOutputDTO> {

    @Column(jdbcType = JdbcType.BIGINT)
    private String userId;

    @Column(jdbcType = JdbcType.BIGINT)
    private String roleId;

}