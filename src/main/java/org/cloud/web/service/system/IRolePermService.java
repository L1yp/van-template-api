package org.cloud.web.service.system;

import io.mybatis.mapper.BaseMapper;
import org.cloud.web.model.DO.system.RolePermDO;

import java.util.List;

public interface IRolePermService {

    BaseMapper<RolePermDO, String> getBaseMapper();

    /**
     * 通过角色ID获取权限标识列表
     * @param roleId 角色ID
     * @return 权限标识列表
     */
    List<String> listPermByRoleId(String roleId);


    /**
     * 通过权限标识获取角色ID列表
     * @param permKey 权限标识符
     * @return 角色ID列表
     */
    List<String> listRoleIdByPermKey(String permKey);

    /**
     * 移除[权限标识 => 角色ID]缓存
     * @param permKey 权限标识
     */
    void evictRoleIdListByPermKey(String permKey);

    /**
     * 移除[角色ID => 权限标识]缓存并删除关联关系
     * @param roleId 角色ID
     */
    void deletePermKeyListByRoleId(String roleId);

}
