package org.cloud.web.service.system.impl;

import jakarta.annotation.Resource;
import org.cloud.exception.BusinessException;
import org.cloud.model.common.PageDTO;
import org.cloud.service.AbstractService;
import org.cloud.util.MessageUtils;
import org.cloud.web.model.DO.system.PermDO;
import org.cloud.web.model.DTO.out.system.PermOutputDTO;
import org.cloud.web.service.system.IPermService;
import org.cloud.web.service.system.IRolePermService;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@CacheConfig(cacheNames = "perm")
public class PermServiceImpl extends AbstractService<PermDO, PermOutputDTO, PageDTO> implements IPermService {

    @Resource
    IRolePermService rolePermService;

    @Override
    @Cacheable(key = "#p0")
    public PermDO getById(String id) {
        return super.getById(id);
    }


    @Override
    @Cacheable(key = "'list'")
    public List<PermOutputDTO> list(PageDTO param) {
        return super.list(param);
    }

    @Cacheable(key = "'key:' + #p0")
    public PermDO getPermByKey(String key) {
        return baseMapper.wrapper().eq(PermDO::getPermKey, key).first().orElse(null);
    }

    public List<PermOutputDTO> listByPermKeyList(Collection<String> permKeyList) {
        PermServiceImpl permService = (PermServiceImpl) AopContext.currentProxy();
        List<PermOutputDTO> result = new ArrayList<>();
        for (String permKey : permKeyList) {
            PermDO permDO = permService.getPermByKey(permKey);
            if (permDO != null) {
                result.add(permDO.convert());
            }
        }
        return result;
    }


    @Transactional
    public void deleteByPermKey(String permKey) {
        PermServiceImpl permService = (PermServiceImpl) AopContext.currentProxy();
        PermDO permDO = permService.getPermByKey(permKey);
        if (permDO == null) {
            throw new BusinessException(404, MessageUtils.getMessage("perm.key.not-found", permKey));
        }

        permService.deleteById(permDO.getId());
    }

    @Override
    protected void prepareDelete(PermDO model) {
        if (StringUtils.hasText(model.getPermKey())) {
            List<String> roleIdList = rolePermService.listRoleIdByPermKey(model.getPermKey());
            if (!CollectionUtils.isEmpty(roleIdList)) {
                throw new BusinessException(500, MessageUtils.getMessage("perm.role.has-bound"));
            }
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'key:' + #p0.permKey"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterDelete(PermDO model) {
        // 角色绑定有角色则不允许删除
        rolePermService.evictRoleIdListByPermKey(model.getPermKey());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'key:' + #p0.permKey"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterUpdate(PermDO paramDO, PermDO modelDO) {
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.id"),
            @CacheEvict(key = "'key:' + #p0.permKey"),
            @CacheEvict(key = "'list'"),
    })
    protected void afterAdd(PermDO model) {
    }

}