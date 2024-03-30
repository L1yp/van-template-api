package org.cloud.web.service.system.impl;

import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.web.model.DO.system.RoleMenuDO;
import org.cloud.web.model.DTO.out.system.RoleMenuDTO;
import org.cloud.web.service.system.IRoleMenuService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "role_menu")
public class RoleMenuServiceImpl extends AbstractService<RoleMenuDO, RoleMenuDTO, PageDTO> implements IRoleMenuService {


    @Cacheable(key = "'role:' + #p0", unless = "#result == null")
    public List<String> listMenuIdByRoleId(String roleId) {
        return baseMapper.wrapper().eq(RoleMenuDO::getRoleId, roleId).list().stream().map(RoleMenuDO::getMenuId).distinct().toList();
    }

    @Cacheable(key = "'menu:' + #p0", unless = "#result == null")
    public List<String> listRoleIdByMenuId(String menuId) {
        return baseMapper.wrapper().eq(RoleMenuDO::getMenuId, menuId).list().stream().map(RoleMenuDO::getRoleId).distinct().toList();
    }

    @CacheEvict(key = "'menu:' + #p0")
    public void evictRoleIdListByMenuId(String menuId) { }

    @CacheEvict(key = "'role:' + #p0")
    public void deleteMenuIdListByRoleId(String roleId) {
        baseMapper.wrapper().eq(RoleMenuDO::getRoleId, roleId).delete();
    }


}
