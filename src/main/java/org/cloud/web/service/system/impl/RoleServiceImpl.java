package org.cloud.web.service.system.impl;

import jakarta.annotation.Resource;
import org.cloud.cache.CacheTTL;
import org.cloud.cache.LocalCache;
import org.cloud.exception.BusinessException;
import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.util.MessageUtils;
import org.cloud.web.mapper.system.RoleMenuMapper;
import org.cloud.web.model.DO.system.PermDO;
import org.cloud.web.model.DO.system.RoleDO;
import org.cloud.web.model.DO.system.RoleMenuDO;
import org.cloud.web.model.DO.system.RolePermDO;
import org.cloud.web.model.DTO.in.system.RoleMenuBindDTO;
import org.cloud.web.model.DTO.in.system.RolePermBindDTO;
import org.cloud.web.model.DTO.out.system.PermOutputDTO;
import org.cloud.web.model.DTO.out.system.RoleOutputDTO;
import org.cloud.web.service.system.IPermService;
import org.cloud.web.service.system.IRoleMenuService;
import org.cloud.web.service.system.IRolePermService;
import org.cloud.web.service.system.IRoleService;
import org.cloud.web.service.system.IUserRoleService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@CacheConfig(cacheNames = "role")
@CacheTTL(7 * 24 * 60 * 60L)
public class RoleServiceImpl extends AbstractService<RoleDO, RoleOutputDTO, PageDTO> implements IRoleService {

    @Resource
    IRolePermService rolePermService;

    @Resource
    IRoleMenuService roleMenuService;

    @Resource
    IPermService permService;

    @Resource
    IUserRoleService userRoleService;

    @Resource
    RoleMenuMapper roleMenuMapper;

    @Override
    @LocalCache
    @Cacheable(key = "#p0", unless = "#result == null")
    public RoleDO getById(String id) {
        return super.getById(id);
    }

    @Override
    @LocalCache
    @Cacheable(key = "'list'", unless = "#result == null")
    public List<RoleOutputDTO> list(PageDTO param) {
        return super.list(param);
    }

    @Override
    protected void prepareDelete(RoleDO model) {
        List<String> userIdList = userRoleService.listUserIdByRoleId(model.getId());
        if (!CollectionUtils.isEmpty(userIdList)) {
            throw new BusinessException(500, MessageUtils.getMessage("role.user.has-bound"));
        }
    }

    @Override
    @LocalCache
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterDelete(RoleDO model) {
        String roleId = model.getId();
        rolePermService.deletePermKeyListByRoleId(roleId);

        roleMenuService.deleteMenuIdListByRoleId(roleId);

        userRoleService.evictUserIdListByRoleId(roleId);

        // 和角色相关的 全部需要在此处删除关联关系
    }

    @Override
    @LocalCache
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterAdd(RoleDO model) { }

    @Override
    @LocalCache
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterUpdate(RoleDO paramDO, RoleDO modelDO) { }

    @Transactional
    public void bindMenu(String loginUserId, RoleMenuBindDTO param) {
        // 查询原来绑定的菜单列表
        List<String> menuIdList = roleMenuService.listMenuIdByRoleId(param.getRoleId());
        List<String> candidateDeleteMenuIdList = new ArrayList<>(menuIdList);

        // 删除原来绑定的
        roleMenuService.deleteMenuIdListByRoleId(param.getRoleId());

        for (String menuId : param.getMenuIds()) {
            candidateDeleteMenuIdList.add(menuId); // 新增

            RoleMenuDO roleMenu = new RoleMenuDO();
            roleMenu.setRoleId(param.getRoleId());
            roleMenu.setMenuId(menuId);
            roleMenu.setCreateBy(loginUserId);
            roleMenu.setCreateTime(LocalDateTime.now());
            roleMenuMapper.insert(roleMenu);
        }

        // 删除 角色 -> 菜单ID[]
        if (!CollectionUtils.isEmpty(candidateDeleteMenuIdList)) {
            // 删除 菜单ID -> 角色ID[]
            candidateDeleteMenuIdList.forEach(roleMenuService::evictRoleIdListByMenuId);
        }

    }

    public List<String> listMenuIdByRoleId(String roleId) {
        return roleMenuService.listMenuIdByRoleId(roleId);
    }

    @Transactional
    public void bindPerm(String loginUserId, RolePermBindDTO param) {
        List<String> permKeyList = rolePermService.listPermByRoleId(param.getRoleId());

        List<String> candidateDeletePermKeyList = new ArrayList<>(permKeyList);

        rolePermService.deletePermKeyListByRoleId(param.getRoleId());
        for (String permKey : param.getPermKeyList()) {
            if (!StringUtils.hasText(permKey)) {
                continue;
            }
            PermDO permDO = permService.getPermByKey(permKey);
            if (permDO == null) {
                continue;
            }

            candidateDeletePermKeyList.add(permKey);

            RolePermDO model = new RolePermDO();
            model.setPerm(permKey);
            model.setRoleId(param.getRoleId());
            model.setCreateBy(loginUserId);
            model.setCreateTime(LocalDateTime.now());
            rolePermService.getBaseMapper().insert(model);
        }
        if (!CollectionUtils.isEmpty(candidateDeletePermKeyList)) {
            candidateDeletePermKeyList.forEach(rolePermService::evictRoleIdListByPermKey);
        }
    }

    public List<PermOutputDTO> listPermByRoleId(String roleId) {
        List<String> permKeyList = rolePermService.listPermByRoleId(roleId);
        if (CollectionUtils.isEmpty(permKeyList)) {
            return Collections.emptyList();
        }
        return permService.listByPermKeyList(permKeyList);
    }


}
