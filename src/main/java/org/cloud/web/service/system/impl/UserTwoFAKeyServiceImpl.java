package org.cloud.web.service.system.impl;

import jakarta.annotation.Resource;
import org.cloud.model.common.PageDTO;
import org.cloud.model.enums.UserTwoFAType;
import org.cloud.service.AbstractService;
import org.cloud.web.mapper.system.UserTwoFAKeyMapper;
import org.cloud.web.model.DO.system.UserTwoFAKeyDO;
import org.cloud.web.model.DTO.out.system.UserTwoFAKeyOutputDTO;
import org.cloud.web.service.system.IUserTwoFAKeyService;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "user_2fa_key")
public class UserTwoFAKeyServiceImpl extends AbstractService<UserTwoFAKeyDO, UserTwoFAKeyOutputDTO, PageDTO> implements IUserTwoFAKeyService {

    @Resource
    UserTwoFAKeyMapper userTwoFAKeyMapper;

    public UserTwoFAKeyServiceImpl getProxy() {
        return (UserTwoFAKeyServiceImpl) AopContext.currentProxy();
    }

    @Override
    @Cacheable(key = "'ut:' + #userId + '_' + #type.value")
    public UserTwoFAKeyDO getByUserIdAndType(String userId, UserTwoFAType type) {
        UserTwoFAKeyDO model = userTwoFAKeyMapper.wrapper()
                .eq(UserTwoFAKeyDO::getUserId, userId)
                .eq(UserTwoFAKeyDO::getType, type.getValue())
                .first().orElse(null);
        if (model != null) {
            getProxy().putIdCache(model);
        }
        return model;
    }


    @Override
    @Cacheable(key = "#id")
    public UserTwoFAKeyDO getById(String id) {
        UserTwoFAKeyDO model = userTwoFAKeyMapper.selectByPrimaryKey(id).orElse(null);
        if (model != null) {
            getProxy().putUserIdTypeCache(model);
        }
        return model;
    }

    @CachePut(key = "'ut:' + #p0.userId + '_' + #p0.type.value")
    public UserTwoFAKeyDO putUserIdTypeCache(UserTwoFAKeyDO model) {
        return model;
    }

    @CachePut(key = "#p0.id")
    public UserTwoFAKeyDO putIdCache(UserTwoFAKeyDO model) {
        return model;
    }


    @Override
    @Caching(evict = {
            @CacheEvict(key = "'ut:' + #p0.userId + '_' + #p0.type.value"),
            @CacheEvict(key = "#p0.id")
    })
    protected void afterAdd(UserTwoFAKeyDO model) {}

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'ut:' + #p0.userId + '_' + #p0.type.value"),
            @CacheEvict(key = "#p0.id")
    })
    protected void afterUpdate(UserTwoFAKeyDO paramDO, UserTwoFAKeyDO modelDO) { }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'ut:' + #p0.userId + '_' + #p0.type.value"),
            @CacheEvict(key = "#p0.id")
    })
    protected void afterDelete(UserTwoFAKeyDO modelDO) { }
}
