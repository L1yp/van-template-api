package org.cloud.web.service.system.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.cloud.exception.BusinessException;
import org.cloud.model.Converter;
import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.util.MessageUtils;
import org.cloud.web.model.DO.system.DepartmentDO;
import org.cloud.web.model.DO.system.UserDO;
import org.cloud.web.model.DTO.out.system.DepartmentOutputDTO;
import org.cloud.web.service.system.IDepartmentService;
import org.cloud.web.service.system.IRoleMenuService;
import org.cloud.web.service.system.IRoleService;
import org.cloud.web.service.system.IUserDepartmentService;
import org.cloud.web.service.system.IUserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@CacheConfig(cacheNames = "dept")
public class DepartmentServiceImpl extends AbstractService<DepartmentDO, DepartmentOutputDTO, PageDTO> implements IDepartmentService {


    @Resource
    IUserDepartmentService userDepartmentService;

    @Resource
    IRoleMenuService roleMenuService;

    @Resource
    IRoleService roleService;

    @Resource
    IUserService userService;

    @Override
    @Cacheable(key = "#p0")
    public DepartmentDO getById(String id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(key = "'list'")
    public List<DepartmentOutputDTO> list() {
        return baseMapper.wrapper().list().stream().map(Converter::convert).toList();
    }

    @Override
    protected void prepareAdd(DepartmentDO model) {
        if (StringUtils.isBlank(model.getPid())) {
            model.setPid(null);
        }
    }

    @Override
    protected void prepareUpdate(DepartmentDO paramDO, DepartmentDO modelDO) {
        if (StringUtils.isBlank(paramDO.getPid())) {
            paramDO.setPid(null);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterAdd(DepartmentDO model) {

    }

    @Override
    protected void prepareDelete(DepartmentDO model) {
        // 校验 用户 是否绑定了部门

        List<String> userIdList = userDepartmentService.listUserIdByDepartmentId(model.getId());
        if (!CollectionUtils.isEmpty(userIdList)) {
            List<String> userNameList = userIdList.stream().map(userService::getById).map(UserDO::getUsername).toList();
            throw new BusinessException(500, MessageUtils.getMessage("dept.secondary.user.has-bound", userNameList));
        }

        long count = userService.getBaseMapper().wrapper().eq(UserDO::getDeptId, model.getId()).count();
        if (count > 0) {
            throw new BusinessException(500, MessageUtils.getMessage("dept.user.has-bound"));
        }

    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterDelete(DepartmentDO modelDO) {
        // 已确认无用户和角色绑定关系
        // 移除部门缓存
        userDepartmentService.evictUserIdListByDepartmentId(modelDO.getId());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterUpdate(DepartmentDO paramDO, DepartmentDO modelDO) { }
}
