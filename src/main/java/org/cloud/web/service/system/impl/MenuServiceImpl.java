package org.cloud.web.service.system.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cloud.cache.CacheTTL;
import org.cloud.cache.LocalCache;
import org.cloud.exception.BusinessException;
import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.util.MessageUtils;
import org.cloud.web.model.DO.system.MenuDO;
import org.cloud.web.model.DO.system.RoleDO;
import org.cloud.web.model.DTO.out.system.MenuOutputDTO;
import org.cloud.web.service.system.IMenuService;
import org.cloud.web.service.system.IRoleMenuService;
import org.cloud.web.service.system.IRoleService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "menu")
@CacheTTL(7 * 24 * 60 * 60L)
public class MenuServiceImpl extends AbstractService<MenuDO, MenuOutputDTO, PageDTO> implements IMenuService {

    @Resource
    IRoleMenuService roleMenuService;

    @Resource
    IRoleService roleService;

    @Override
    @LocalCache
    @Cacheable(key = "'list'")
    public List<MenuOutputDTO> list(PageDTO param) {
        return super.list(param);
    }

    @Override
    @LocalCache
    @Cacheable(key = "#p0")
    public MenuDO getById(String id) {
        return super.getById(id);
    }

    @Override
    protected void prepareAdd(MenuDO model) {
        if (!StringUtils.hasText(model.getPid())) {
            model.setPid(null);
        }
    }

    @Override
    protected void prepareUpdate(MenuDO paramDO, MenuDO modelDO) {
        if (!StringUtils.hasText(paramDO.getPid())) {
            paramDO.setPid(null);
        }
    }

    @Override
    @LocalCache
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterAdd(MenuDO model) { }

    @Override
    @LocalCache
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterUpdate(MenuDO paramDO, MenuDO modelDO) { }


    @Override
    protected void prepareDelete(MenuDO model) {

        // 校验角色和用户是否绑定了部门
        List<String> roleIdList = roleMenuService.listRoleIdByMenuId(model.getId());
        if (!CollectionUtils.isEmpty(roleIdList)) {
            List<String> roleNameList = roleIdList.stream().map(roleService::getById).map(RoleDO::getName).toList();
            throw new BusinessException(500, MessageUtils.getMessage("menu.role-bound", roleNameList));
        }

    }

    @Override
    @LocalCache
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterDelete(MenuDO t) {
        roleMenuService.evictRoleIdListByMenuId(t.getId());
    }


}
