package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractWithUpdateModel;
import org.cloud.model.enums.CommonStatus;
import org.cloud.web.model.DTO.out.system.DepartmentOutputDTO;

@Getter
@Setter
@Table(value = "sys_dept", autoResultMap = true)
public class DepartmentDO extends AbstractWithUpdateModel<DepartmentOutputDTO> {

    private String code;
    private String name;
    private String simpleName;
    private String description;
    private Integer orderNo;
    @Column(jdbcType = JdbcType.BIGINT)
    private String pid;
    @Column(jdbcType = JdbcType.BIGINT)
    private String owner;
    @Column(jdbcType = JdbcType.BIGINT)
    private String assistant;
    private String phone;
    private String officeLocation;
    private String address;
    private CommonStatus status;

}
