package org.cloud.web.service.system.impl;

import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.web.model.DO.system.UserDepartmentDO;
import org.cloud.web.model.DTO.out.system.UserDepartmentOutputDTO;
import org.cloud.web.service.system.IUserDepartmentService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "user_dept")
public class UserDepartmentServiceImpl extends AbstractService<UserDepartmentDO, UserDepartmentOutputDTO, PageDTO> implements IUserDepartmentService {



    @Cacheable(key = "'user:' + #p0")
    public List<String> listDepartmentIdByUserId(String userId) {
        return baseMapper.wrapper()
                .eq(UserDepartmentDO::getUserId, userId)
//                .eq(UserDepartmentDO::getType, UserDeptType.SECONDARY.getValue())
                .list()
                .stream()
                .map(UserDepartmentDO::getDepartmentId)
                .distinct()
                .toList();
    }

    @Cacheable(key = "'dept:' + #p0")
    public List<String> listUserIdByDepartmentId(String departmentId) {
        return baseMapper.wrapper().eq(UserDepartmentDO::getDepartmentId, departmentId).list().stream().map(UserDepartmentDO::getUserId).distinct().toList();
    }

    @CacheEvict(key = "'user:' + #p0")
    public void deleteDepartmentIdListByUserId(String userId) {
        baseMapper.wrapper().eq(UserDepartmentDO::getUserId, userId).delete();
    }

    @CacheEvict(key = "'dept:' + #p0")
    public void evictUserIdListByDepartmentId(String departmentId) { }



}
