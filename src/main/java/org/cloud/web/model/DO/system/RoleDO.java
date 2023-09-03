package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractWithUpdateModel;
import org.cloud.model.enums.CommonStatus;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.web.model.DTO.out.system.RoleOutputDTO;

@Getter
@Setter
@Table(value = "sys_role", autoResultMap = true)
public class RoleDO extends AbstractWithUpdateModel<RoleOutputDTO> {

    private String name;

    private String category;

    private Integer orderNo;

    @Column(typeHandler = BasicEnumTypeHandler.class)
    private CommonStatus status;


}
