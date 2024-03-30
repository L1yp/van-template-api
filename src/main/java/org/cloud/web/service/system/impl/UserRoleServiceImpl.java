package org.cloud.web.service.system.impl;

import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.web.model.DO.system.UserRoleDO;
import org.cloud.web.model.DTO.out.system.UserRoleOutputDTO;
import org.cloud.web.service.system.IUserRoleService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "user_role")
public class UserRoleServiceImpl extends AbstractService<UserRoleDO, UserRoleOutputDTO, PageDTO> implements IUserRoleService {

    @Cacheable(key = "'user:' + #p0", unless = "#result == null")
    public List<String> listRoleIdByUserId(String userId) {
        return baseMapper.wrapper().eq(UserRoleDO::getUserId, userId).list().stream().map(UserRoleDO::getRoleId).distinct().toList();
    }

    @Cacheable(key = "'role:' + #p0", unless = "#result == null")
    public List<String> listUserIdByRoleId(String roleId) {
        return baseMapper.wrapper().eq(UserRoleDO::getRoleId, roleId).list().stream().map(UserRoleDO::getUserId).distinct().toList();
    }

    @CacheEvict(key = "'user:' + #p0")
    public void deleteRoleIdListByUserId(String userId) {
        baseMapper.wrapper().eq(UserRoleDO::getUserId, userId).delete();
    }
    
    @CacheEvict(key = "'role:' + #p0")
    public void evictUserIdListByRoleId(String roleId) { }



}
