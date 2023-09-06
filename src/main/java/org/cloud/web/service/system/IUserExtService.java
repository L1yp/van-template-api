package org.cloud.web.service.system;

import org.cloud.web.model.DTO.out.system.UserLoginResultDTO;

public interface IUserExtService {

    UserLoginResultDTO processLoginResult(UserLoginResultDTO loginResult);

}
