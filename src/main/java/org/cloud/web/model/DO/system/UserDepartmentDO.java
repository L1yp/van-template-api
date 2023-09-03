package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractModel;
import org.cloud.model.enums.UserDeptType;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.web.model.DTO.out.system.UserDepartmentOutputDTO;

@Getter
@Setter
@Table(value = "sys_user_dept", autoResultMap = true)
public class UserDepartmentDO extends AbstractModel<UserDepartmentOutputDTO> {

    @Column(jdbcType = JdbcType.BIGINT)
    private String userId;

    @Column(jdbcType = JdbcType.BIGINT)
    private String departmentId;

    @Column(jdbcType = JdbcType.INTEGER, typeHandler = BasicEnumTypeHandler.class)
    private UserDeptType type;

}
