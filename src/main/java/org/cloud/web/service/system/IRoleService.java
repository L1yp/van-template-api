package org.cloud.web.service.system;

import io.mybatis.mapper.BaseMapper;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.Converter;
import org.cloud.model.common.PageDTO;
import org.cloud.web.model.DO.system.RoleDO;
import org.cloud.web.model.DTO.in.system.RoleMenuBindDTO;
import org.cloud.web.model.DTO.in.system.RolePermBindDTO;
import org.cloud.web.model.DTO.out.system.PermOutputDTO;
import org.cloud.web.model.DTO.out.system.RoleOutputDTO;

import java.util.List;

public interface IRoleService {

    BaseMapper<RoleDO, String> getBaseMapper();

    /**
     * 通过角色ID获取角色信息
     * @param id 角色ID
     * @return 角色信息
     */
    RoleDO getById(String id);

    /**
     * 获取所有角色
     * @param param ignored
     * @return 所有角色列表
     */
    List<RoleOutputDTO> list(PageDTO param);

    /**
     * 获取角色绑定的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<String> listMenuIdByRoleId(String roleId);

    /**
     * 绑定权限
     * @param loginUserId 登录用户ID
     * @param param 权限绑定信息
     */
    void bindPerm(String loginUserId, RolePermBindDTO param);

    /**
     * 通过角色ID获取权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermOutputDTO> listPermByRoleId(String roleId);


    void add(Converter<RoleDO> param);

    void update(AbstractUpdateDTO<RoleDO> param);



    void deleteById(String id);


    void bindMenu(String loginUserId, RoleMenuBindDTO param);
}