package org.cloud.web.service.system;

import org.cloud.model.common.PageDTO;
import org.cloud.model.common.PageData;
import org.cloud.web.model.DO.system.UserLoginLogDO;
import org.cloud.web.model.DTO.out.system.UserLoginLogOutputDTO;

public interface IUserLoginLogService {

    void insert(UserLoginLogDO model);

    PageData<UserLoginLogOutputDTO> page(PageDTO param);

}
