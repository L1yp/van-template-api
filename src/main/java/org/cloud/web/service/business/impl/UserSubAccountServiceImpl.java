package org.cloud.web.service.business.impl;

import jakarta.annotation.Resource;
import org.cloud.exception.BusinessException;
import org.cloud.model.Converter;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.DeductMode;
import org.cloud.util.SpringContext;
import org.cloud.web.mapper.primary.system.UserMapper;
import org.cloud.web.model.DO.business.UserPointDO;
import org.cloud.web.model.DO.system.UserDO;
import org.cloud.web.model.DTO.in.business.UserSubAccountAddDTO;
import org.cloud.web.model.DTO.in.business.UserSubAccountDisabledDTO;
import org.cloud.web.model.DTO.in.system.UserRoleBindDTO;
import org.cloud.web.model.DTO.out.system.UserOutputDTO;
import org.cloud.web.service.business.IUserSubAccountService;
import org.cloud.web.service.system.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Service
public class UserSubAccountServiceImpl implements IUserSubAccountService {

    @Resource
    UserServiceImpl userService;


    @Value("${role.default:2}")
    String defaultRoleId;

    @Override
    public List<UserOutputDTO> getSubAccountList(String loginUserId) {
        return userService.getBaseMapper().wrapper().eq(UserDO::getParentId, loginUserId).list().stream().map(Converter::convert).toList();
    }

    @Override
    @Transactional
    public void addSubAccount(UserSubAccountAddDTO param) {
        UserDO userDO = param.convert();
        userService.getProxy().insertSelective(userDO);
        UserPointServiceImpl userPointService = SpringContext.getBean(UserPointServiceImpl.class);
        Assert.notNull(userPointService, "找不到service");
        UserPointDO userPointDO = new UserPointDO();
        userPointDO.setId(userDO.getId());

        UserRoleBindDTO bindDTO = new UserRoleBindDTO();
        bindDTO.setUserId(userDO.getId());
        bindDTO.setRoleIdList(List.of(defaultRoleId));
        userService.getProxy().bindRole(bindDTO);

        userPointDO.setDeductMode(DeductMode.valueOf(param.getDeductMode()));
        userPointDO.setPoint(0L);
        userPointService.insertSelective(userPointDO);
    }

    @Override
    public void changeSubAccountState(UserSubAccountDisabledDTO param, CommonStatus status) {
        UserDO userDO = userService.assertIdPresentAndGetDO(param.getUserId());
        if (!Objects.equals(userDO.getParentId(), param.getLoginUserId())) {
            throw new BusinessException(400, "目标帐号不是你的子帐号");
        }

        if (userDO.getStatus() == status) {
            throw new BusinessException(400, "目标帐号已" + status.description());
        }

        UserDO updateDO = new UserDO();
        updateDO.setId(param.getUserId());
        updateDO.setStatus(status);

        // 更新状态
        userService.getProxy().updateByPrimaryKeySelective(updateDO);
    }
}
