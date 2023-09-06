package org.cloud.web.service.system;

import io.mybatis.mapper.BaseMapper;
import org.cloud.web.model.DO.system.UserRoleDO;

import java.util.List;

public interface IUserRoleService {

    void insert(UserRoleDO modelDO);

    BaseMapper<UserRoleDO, String> getBaseMapper();

    /**
     * 通过用户ID获取角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<String> listRoleIdByUserId(String userId);



    /**
     * 通过角色ID获取用户ID列表
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<String> listUserIdByRoleId(String roleId);

    /**
     * 清空 用户-角色 缓存及删除用户的关联关系
     * @param userId 用户ID
     */
    void deleteRoleIdListByUserId(String userId);

    /**
     * 清空 角色-用户 缓存
     * @param roleId 角色ID
     */
    void evictUserIdListByRoleId(String roleId);

}
