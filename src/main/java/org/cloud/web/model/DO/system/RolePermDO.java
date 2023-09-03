package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractModel;
import org.cloud.web.model.DTO.out.system.RolePermOutputDTO;

@Getter
@Setter
@Table(value = "sys_role_perm", autoResultMap = true)
public class RolePermDO extends AbstractModel<RolePermOutputDTO> {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限标识符
     */
    private String perm;


}