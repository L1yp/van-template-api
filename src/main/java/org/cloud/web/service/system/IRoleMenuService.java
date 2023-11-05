package org.cloud.web.service.system;

import org.cloud.mybatis.Mapper;
import org.cloud.web.model.DO.system.RoleMenuDO;

import java.util.List;

public interface IRoleMenuService {

    Mapper<RoleMenuDO, String> getBaseMapper();

    /**
     * 通过角色ID获取菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<String> listMenuIdByRoleId(String roleId);


    /**
     * 通过菜单ID获取角色ID列表
     * @param menuId 菜单ID
     * @return 角色ID列表
     */
    List<String> listRoleIdByMenuId(String menuId);


    /**
     * 删除[菜单id => 角色ID列表]缓存
     * @param menuId 菜单ID
     */
    void evictRoleIdListByMenuId(String menuId);

    /**
     * 删除[角色ID => 菜单ID列表]缓存
     * @param roleId 角色ID
     */
    void deleteMenuIdListByRoleId(String roleId);

}
