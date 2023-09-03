package org.cloud.web.service.system;

import io.mybatis.mapper.BaseMapper;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.Converter;
import org.cloud.model.common.PageData;
import org.cloud.web.model.DO.system.UserDO;
import org.cloud.web.model.DTO.in.system.UserAddDTO;
import org.cloud.web.model.DTO.in.system.UserChangePwdDTO;
import org.cloud.web.model.DTO.in.system.UserDeptBindDTO;
import org.cloud.web.model.DTO.in.system.UserLoginDTO;
import org.cloud.web.model.DTO.in.system.UserQueryPageDTO;
import org.cloud.web.model.DTO.in.system.UserRegisterDTO;
import org.cloud.web.model.DTO.in.system.UserRoleBindDTO;
import org.cloud.web.model.DTO.in.system.UserUpdateDTO;
import org.cloud.web.model.DTO.out.system.MenuOutputDTO;
import org.cloud.web.model.DTO.out.system.UserLoginResultDTO;
import org.cloud.web.model.DTO.out.system.UserOutputDTO;

import java.util.List;

public interface IUserService {

    BaseMapper<UserDO, String> getBaseMapper();

    IUserService getProxy();

    /**
     * 通过用户ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    UserDO getById(String id);

    /**
     * 通过用户名获取用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    UserDO getByUserName(String userName);

    UserDO putIdCache(String userId, UserDO model);

    UserDO putUserNameCache(String userName, UserDO model);

    /**
     * 用户分页列表
     * @param param
     * @return 分页列表
     */
    PageData<UserOutputDTO> page(UserQueryPageDTO param);

    /**
     * 用户登录
     * @param param 登录参数
     * @return 登录结果
     */
    UserLoginResultDTO login(UserLoginDTO param);

    /**
     * 获取登录状态信息
     * @param userId  登录用户ID
     * @return 状态信息
     */
    UserLoginResultDTO getLoginResult(String userId);

    /**
     * 修改密码
     * @param param 修改密码参数
     */
    void changePwd(UserChangePwdDTO param);

    /**
     * 新增用户
     * @param model 用户数据
     */
    void add(UserAddDTO model);

    void add(UserDO model);

    /**
     * 用户注册
     * @param param
     */
    void register(UserRegisterDTO param);

    /**
     * 获取用户绑定的菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<MenuOutputDTO> getMenuList(String userId);

    /**
     * 查询用户绑定的角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<String> getRoleIdList(String userId);

    /**
     * 查询用户绑定的兼职部门列表
     * @param userId 用户ID
     * @return 兼职部门ID列表
     */
    List<String> getDeptIdList(String userId);

    /**
     * 查询用户绑定的菜单ID列表
     * @param userId 用户ID
     * @return 菜单ID列表
     */
    List<String> findUserMenuIdList(String userId);

    /**
     * 查询用户绑定的权限key列表
     * @param loginUserId 用户ID
     * @return 权限key列表
     */
    List<String> getPermKeyList(String loginUserId);

    /**
     * 绑定角色
     * @param loginUserId 操作者
     * @param param 角色绑定参数
     */
    void bindRole(UserRoleBindDTO param);

    /**
     * 绑定部门
     * @param param 部门绑定参数
     */
    void bindDept(UserDeptBindDTO param);

    void deleteById(String id);

    List<UserOutputDTO> list(UserQueryPageDTO param);

    void update(UserUpdateDTO param);
}
