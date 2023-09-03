package org.cloud.web.service.system.impl;

import org.cloud.cache.CacheTTL;
import org.cloud.cache.LocalCache;
import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.web.model.DO.system.RolePermDO;
import org.cloud.web.model.DTO.out.system.RolePermOutputDTO;
import org.cloud.web.service.system.IRolePermService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "role_perm")
@CacheTTL(7 * 24 * 60 * 60L)
public class RolePermServiceImpl extends AbstractService<RolePermDO, RolePermOutputDTO, PageDTO> implements IRolePermService {


    @LocalCache
    @Cacheable(key = "'role:' + #p0", unless = "#result == null")
    public List<String> listPermByRoleId(String roleId) {
        return baseMapper.wrapper().eq(RolePermDO::getRoleId, roleId).list().stream().map(RolePermDO::getPerm).distinct().toList();
    }


    @LocalCache
    @Cacheable(key = "'perm:' + #p0", unless = "#result == null")
    public List<String> listRoleIdByPermKey(String permKey) {
        return baseMapper.wrapper().eq(RolePermDO::getPerm, permKey).list().stream().map(RolePermDO::getRoleId).distinct().toList();
    }

    @LocalCache
    @CacheEvict(key = "'perm:' + #p0")
    public void evictRoleIdListByPermKey(String permKey) { }

    @LocalCache
    @CacheEvict(key = "'role:' + #p0")
    public void deletePermKeyListByRoleId(String roleId) {
        baseMapper.wrapper().eq(RolePermDO::getRoleId, roleId).delete();
    }



}