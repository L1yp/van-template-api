package org.cloud.web.service.system;

import io.mybatis.mapper.BaseMapper;
import org.cloud.web.model.DO.system.UserDepartmentDO;

import java.util.List;

public interface IUserDepartmentService {

    BaseMapper<UserDepartmentDO, String> getBaseMapper();


    /**
     * 根据用户ID查询兼职部门ID列表
     * @param userId 用户ID
     * @return 用户绑定的兼职部门ID列表
     */
    List<String> listDepartmentIdByUserId(String userId);


    /**
     * 根据部门ID查询绑定的用户列表
     * @param departmentId 部门ID
     * @return 部门ID绑定的用户列表
     */
    List<String> listUserIdByDepartmentId(String departmentId);


    /**
     * 删除 用户-兼职部门 的缓存及用户绑定的部门
     * @param userId 用户ID
     */
    void deleteDepartmentIdListByUserId(String userId);


    /**
     * 删除 兼职部门-用户 的缓存
     * @param departmentId 部门ID
     */
    void evictUserIdListByDepartmentId(String departmentId);

    void insert(UserDepartmentDO model);

}
