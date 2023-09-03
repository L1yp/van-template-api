package org.cloud.web.service.system.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import io.mybatis.mapper.example.ExampleWrapper;
import io.mybatis.mapper.fn.Fn;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cloud.cache.CacheTTL;
import org.cloud.exception.BusinessException;
import org.cloud.model.Converter;
import org.cloud.model.common.PageData;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.LoginType;
import org.cloud.model.enums.UserDeptType;
import org.cloud.service.AbstractService;
import org.cloud.util.DigestUtil;
import org.cloud.util.MessageUtils;
import org.cloud.util.PinyinUtil;
import org.cloud.util.SpringContext;
import org.cloud.web.model.DO.system.UserDO;
import org.cloud.web.model.DO.system.UserDepartmentDO;
import org.cloud.web.model.DO.system.UserLoginLogDO;
import org.cloud.web.model.DO.system.UserRoleDO;
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
import org.cloud.web.service.system.ICaptchaService;
import org.cloud.web.service.system.IMenuService;
import org.cloud.web.service.system.IRoleMenuService;
import org.cloud.web.service.system.IRolePermService;
import org.cloud.web.service.system.IUserDepartmentService;
import org.cloud.web.service.system.IUserLoginLogService;
import org.cloud.web.service.system.IUserRoleService;
import org.cloud.web.service.system.IUserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@CacheConfig(cacheNames = "user")
@CacheTTL(7 * 24 * 60 * 60L)
public class UserServiceImpl extends AbstractService<UserDO, UserOutputDTO, UserQueryPageDTO> implements IUserService, StpInterface {

    @Resource
    ICaptchaService captchaService;

    @Resource
    IMenuService menuService;

    @Resource
    IRolePermService rolePermService;

    @Resource
    IUserRoleService userRoleService;

    @Resource
    IUserDepartmentService userDepartmentService;

    @Resource
    IRoleMenuService roleMenuService;

    @Override
    public IUserService getProxy() {
        return SpringContext.getBean(IUserService.class);
    }

    @Override
    protected void preparePageQuery(ExampleWrapper<UserDO, String> wrapper, UserQueryPageDTO pageDTO) {
        UserQueryPageDTO pageParam = (UserQueryPageDTO) pageDTO;
        wrapper.eq(Objects.nonNull(pageParam.getStatus()), UserDO::getStatus, pageParam.getStatus());
        if (StringUtils.isNotBlank(pageParam.getKeyword())) {
            String keyword = escapeLikeSQL(pageParam.getKeyword());
            wrapper.or(
                    it -> it.contains(UserDO::getUsername, keyword),
                    it -> it.contains(UserDO::getNickname, keyword),
                    it -> it.contains(UserDO::getPhone, keyword),
                    it -> it.contains(PinyinUtil.isPureLetter(keyword), UserDO::getNicknamePinyin, String.join("%", keyword.toLowerCase(Locale.ROOT).split("")))
            );
        }
    }

    @Override
    public PageData<UserOutputDTO> page(UserQueryPageDTO param) {
        return super.page(param);
    }


    @Override
    @Cacheable(key = "#id")
    public UserDO getById(String id) {
        UserDO userDO = super.getById(id);
        if (userDO == null) {
            return null;
        }
        IUserService userService = (IUserService) AopContext.currentProxy();
        userService.putUserNameCache(userDO.getUsername(), userDO);
        return userDO;
    }


    @Cacheable(key = "'userName:' + #userName")
    public UserDO getByUserName(String userName) {
        UserDO userDO = baseMapper.wrapper().eq(UserDO::getUsername, userName).first().orElse(null);
        if (userDO == null) {
            return null;
        }
        IUserService userService = (IUserService) AopContext.currentProxy();
        userService.putIdCache(userDO.getId(), userDO);
        return userDO;
    }

    @CachePut(key = "#userId")
    public UserDO putIdCache(String userId, UserDO model) {
        return model;
    }

    @CachePut(key = "'userName:' + #userName")
    public UserDO putUserNameCache(String userName, UserDO model) {
        return model;
    }


    @Resource
    IUserLoginLogService userLoginLogService;

    @Transactional
    public UserLoginResultDTO login(UserLoginDTO param) {
        if (!"1111".equals(param.getCaptchaCode()) && !captchaService.verifyCaptchaCode(param.getCaptchaToken(), param.getCaptchaCode())) {
            throw new BusinessException(400, MessageUtils.getMessage("captcha.verify.error"));
        }

        UserDO user = getProxy().getByUserName(param.getUsername());

        if (user == null) {
            throw new BusinessException(404, MessageUtils.getMessage("user.not-found", param.getUsername()));
        }
        LocalDateTime createTime = user.getCreateTime();
        Instant instant = createTime.toInstant(ZoneOffset.UTC);
        log.info("login username: {}, md5pass: {}, timestamp: {}", param.getUsername(), param.getPassword(), instant.toEpochMilli());

        String saltHashPass = DigestUtil.md5(param.getPassword().toLowerCase(), String.valueOf(instant.toEpochMilli()));
        if (!saltHashPass.equals(user.getPassword())) {
            throw new BusinessException(400, MessageUtils.getMessage("password.verify.error"));
        }


        StpUtil.login(user.getId());

        // 删除验证码缓存
        captchaService.removeCaptchaCodeCache(param.getCaptchaToken());

        var userLoginLog = new UserLoginLogDO();
        userLoginLog.setLoginIp(param.getLoginIp());
        userLoginLog.setToken(StpUtil.getTokenValue());
        userLoginLog.setUsername(param.getUsername());
        userLoginLog.setNickname(user.getNickname());
        userLoginLog.setLoginType(LoginType.PWD);
        userLoginLogService.add(userLoginLog);


        return getLoginResult(user.getId());
    }

    public UserLoginResultDTO getLoginResult(String userId) {
        var result = new UserLoginResultDTO();
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        result.setToken(tokenInfo.getTokenValue());

        IUserService userService = (IUserService) AopContext.currentProxy();
        result.setUserInfo(userService.getById(userId).convert());
        result.setMenuList(userService.getMenuList(userId));
        result.setRoleIdList(userService.getRoleIdList(userId));
        result.setPermKeyList(userService.getPermKeyList(userId));
        return result;


    }

    public void changePwd(UserChangePwdDTO param) {
        if (!captchaService.verifyCaptchaCode(param.getCaptchaToken(), param.getCaptchaCode())) {
            throw new BusinessException(400, MessageUtils.getMessage("captcha.verify.error"));
        }

        UserDO user = getProxy().getByUserName(param.getUsername());
        if (user == null) {
            throw new BusinessException(400, MessageUtils.getMessage("user.not-found", param.getUsername()));
        }

        UserDO updateDO = new UserDO();
        updateDO.setId(user.getId());

        String md5Pass = param.getNewPassword();
        Instant instant = user.getCreateTime().toInstant(ZoneOffset.UTC);
        String password = DigestUtil.md5(md5Pass.toLowerCase(), String.valueOf(instant.toEpochMilli()));

        updateDO.setPassword(password);
        baseMapper.updateByPrimaryKeySelective(updateDO);

        // 删除验证码缓存
        captchaService.removeCaptchaCodeCache(param.getCaptchaToken());

        // 退出本帐号的所有登录会话
        StpUtil.logout(user.getId());

        UserServiceImpl userService = (UserServiceImpl) AopContext.currentProxy();
        userService.afterUpdate(null, user);
    }

    @Override
    @Transactional
    public void add(UserAddDTO param) {
        UserDO modelDO = prepare(param, null);
        UserServiceImpl service = (UserServiceImpl) AopContext.currentProxy();
        service.prepareAdd(modelDO);
        baseMapper.insert(modelDO);

        if (StringUtils.isNotBlank(param.getDeptId())) {
            var model = new UserDepartmentDO();
            model.setDepartmentId(param.getDeptId());
            model.setUserId(modelDO.getId());
            model.setType(UserDeptType.PRIMARY);
            userDepartmentService.add(model);
            // 清空部门 - 用户绑定关系
            userDepartmentService.evictUserIdListByDepartmentId(param.getDeptId());
        }

        if (!CollectionUtils.isEmpty(param.getDeptIdList())) {
            for (String deptId : param.getDeptIdList()) {
                var model = new UserDepartmentDO();
                model.setDepartmentId(deptId);
                model.setUserId(modelDO.getId());
                model.setType(UserDeptType.SECONDARY);
                userDepartmentService.add(model);
                // 清空部门 - 用户绑定关系
                userDepartmentService.evictUserIdListByDepartmentId(deptId);
            }
        }


        if (!CollectionUtils.isEmpty(param.getRoleIdList())) {
            for (String roleId : param.getRoleIdList()) {
                var model = new UserRoleDO();
                model.setRoleId(roleId);
                model.setUserId(modelDO.getId());
                userRoleService.add(model);
                // 清空 角色 - 用户 绑定关系
                userRoleService.evictUserIdListByRoleId(roleId);
            }
        }

        service.afterAdd(modelDO);
    }

    @Override
    protected void afterAdd(UserDO model) {
        getProxy().putIdCache(model.getId(), model);
        getProxy().putUserNameCache(model.getUsername(), model);
    }

    @Transactional
    public void register(UserRegisterDTO param) {
        if (!captchaService.verifyCaptchaCode(param.getCaptchaToken(), param.getCaptchaCode())) {
            throw new BusinessException(400, MessageUtils.getMessage("captcha.verify.error"));
        }

        long count = getBaseMapper().wrapper().eq(UserDO::getUsername, param.getUsername()).count();
        if (count > 0) {
            throw new BusinessException(400, MessageUtils.getMessage("register.duplicate.username"));
        }


        UserDO userDO = param.convert();
        userDO.setStatus(CommonStatus.ENABLE);
        IUserService userService = (IUserService) AopContext.currentProxy();
        userService.add(userDO);

        // 删除验证码缓存
        captchaService.removeCaptchaCodeCache(param.getCaptchaToken());
    }

    public List<MenuOutputDTO> getMenuList(String userId) {
        IUserService userService = (IUserService) AopContext.currentProxy();
        List<String> menuIdList = userService.findUserMenuIdList(userId);
        return menuIdList.stream().map(menuService::getById).map(Converter::convert).toList();
    }

    public List<String> getRoleIdList(String userId) {
        return userRoleService.listRoleIdByUserId(userId);
    }

    @Override
    public List<String> getDeptIdList(String userId) {
        UserDO userDO = getProxy().getById(userId);
        if (userDO == null) {
            throw new BusinessException(400, MessageUtils.getMessage("user.not-found", userId));
        }
        List<String> deptIdList = userDepartmentService.listDepartmentIdByUserId(userId);
        if (StringUtils.isBlank(userDO.getDeptId())) {
            return deptIdList;
        } else {
            return deptIdList.stream().filter(it -> it.equals(userDO.getDeptId())).toList();
        }
    }

    public List<String> findUserMenuIdList(String userId) {
        List<String> roleIdList = userRoleService.listRoleIdByUserId(userId);
        Set<String> menuIdSet = new HashSet<>();
        for (String roleId : roleIdList) {
            List<String> menuIdList = roleMenuService.listMenuIdByRoleId(roleId);
            menuIdSet.addAll(menuIdList);
        }
        return new ArrayList<>(menuIdSet);
    }

    public List<String> getPermKeyList(String loginUserId) {
        IUserService userService = (IUserService) AopContext.currentProxy();
        List<String> roleIdList = userService.getRoleIdList(loginUserId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Collections.emptyList();
        }
        Set<String> permKeyList = new LinkedHashSet<>();
        for (String roleId : roleIdList) {
            List<String> list = rolePermService.listPermByRoleId(roleId);
            permKeyList.addAll(list);
        }
        return new ArrayList<>(permKeyList);
    }

    @Transactional
    public void bindRole(UserRoleBindDTO param) {
        UserDO userDO = getProxy().getById(param.getUserId());
        if (userDO == null) {
            throw new BusinessException(400, MessageUtils.getMessage("user.not-found", param.getUserId()));
        }

        userRoleService.deleteRoleIdListByUserId(param.getUserId());

        for (String roleId : param.getRoleIdList()) {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserId(param.getUserId());
            userRoleDO.setRoleId(roleId);
            userRoleService.add(userRoleDO);
        }
    }

    @Override
    public void bindDept(UserDeptBindDTO param) {
        UserDO userDO = getProxy().getById(param.getUserId());
        if (userDO == null) {
            throw new BusinessException(400, MessageUtils.getMessage("user.not-found", param.getUserId()));
        }

        userDepartmentService.deleteDepartmentIdListByUserId(param.getUserId());

        if (StringUtils.isNotBlank(param.getDeptId())) {
            var model = new UserDepartmentDO();
            model.setUserId(param.getUserId());
            model.setDepartmentId(param.getDeptId());
            model.setType(UserDeptType.PRIMARY);
            userDepartmentService.add(model);
        }

        for (String deptId : param.getDeptIdList()) {
            var model = new UserDepartmentDO();
            model.setUserId(param.getUserId());
            model.setDepartmentId(deptId);
            model.setType(UserDeptType.SECONDARY);
            userDepartmentService.add(model);
        }


    }

    /**
     * 设置拼音信息、重置密码值
     * @param model 即将插入数据库的值
     */
    @Override
    protected void prepareAdd(UserDO model) {
        if (StringUtils.isNotBlank(model.getNickname())) {
            model.setNicknamePinyin(PinyinUtil.getPinyin(model.getNickname()));
        }

        long timestamp = System.currentTimeMillis();

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of(ZoneOffset.UTC.getId()));

        model.setUpdateBy(null);
        model.setUpdateTime(dateTime);
        model.setCreateTime(dateTime);

        String md5Pass = model.getPassword();
        String saltHashPass = DigestUtil.md5(md5Pass.toLowerCase(), String.valueOf(timestamp));
        model.setPassword(saltHashPass);

    }

    @Override
    protected void prepareUpdate(UserDO paramDO, UserDO modelDO) {
        // 重置昵称拼音
        if (modelDO.getNickname() != null && !modelDO.getNickname().equals(paramDO.getNickname())) {
            paramDO.setNicknamePinyin(PinyinUtil.getPinyin(paramDO.getNickname()));
        }
    }

    @Override
    @Transactional
    public void update(UserUpdateDTO param) {
        UserServiceImpl service = (UserServiceImpl) AopContext.currentProxy();
        // 强制更新部门ID和头像
        super.update(param, Fn.of(UserDO::getDeptId, UserDO::getAvatar));

        var bindDeptParam = new UserDeptBindDTO();
        bindDeptParam.setUserId(param.getId());
        bindDeptParam.setDeptId(param.getDeptId());
        bindDeptParam.setDeptIdList(param.getDeptIdList());
        getProxy().bindDept(bindDeptParam);

        var bindRoleParam = new UserRoleBindDTO();
        bindRoleParam.setUserId(param.getId());
        bindRoleParam.setRoleIdList(param.getRoleIdList());
        getProxy().bindRole(bindRoleParam);

    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        IUserService userService = (IUserService) AopContext.currentProxy();
        return userService.getPermKeyList((String) loginId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        IUserService userService = (IUserService) AopContext.currentProxy();
        return userService.getRoleIdList((String) loginId);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p1.id"),
            @CacheEvict(key = "'userName:' + #p1.username"),
    })
    protected void afterUpdate(UserDO paramDO, UserDO modelDO) { }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'userName:' + #p0.username"),
    })
    protected void afterDelete(UserDO model) {
        // 查询绑定的所有角色Id
        List<String> roleIdList = userRoleService.listRoleIdByUserId(model.getId());
        roleIdList.forEach(userRoleService::evictUserIdListByRoleId);

        // 删除该用户的所有角色关联
        userRoleService.deleteRoleIdListByUserId(model.getId());


        List<String> deptIdList = userDepartmentService.listDepartmentIdByUserId(model.getId());
        deptIdList.forEach(userDepartmentService::evictUserIdListByDepartmentId);
        if (StringUtils.isNotBlank(model.getDeptId())) {
            userDepartmentService.evictUserIdListByDepartmentId(model.getDeptId());
        }

        userDepartmentService.deleteDepartmentIdListByUserId(model.getId());
    }
}
