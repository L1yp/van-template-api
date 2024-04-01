package org.cloud.web.service.business;

import org.cloud.model.enums.CommonStatus;
import org.cloud.web.model.DTO.in.business.UserSubAccountAddDTO;
import org.cloud.web.model.DTO.in.business.UserSubAccountDisabledDTO;
import org.cloud.web.model.DTO.out.system.UserOutputDTO;

import java.util.List;

public interface IUserSubAccountService {

    /**
     * 获取指定用户的子帐号列表
     * @param loginUserId 登录用户
     * @return 子帐号列表
     */
    List<UserOutputDTO> getSubAccountList(String loginUserId);


    void addSubAccount(UserSubAccountAddDTO param);

    /**
     *
     * @param param
     * @param status
     */
    void changeSubAccountState(UserSubAccountDisabledDTO param, CommonStatus status);



}
