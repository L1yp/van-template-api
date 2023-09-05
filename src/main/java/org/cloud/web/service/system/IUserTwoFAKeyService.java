package org.cloud.web.service.system;

import org.cloud.model.enums.UserThirdAuthType;
import org.cloud.model.enums.UserTwoFAType;
import org.cloud.web.model.DO.system.UserTwoFAKeyDO;

public interface IUserTwoFAKeyService {


    UserTwoFAKeyDO getByUserIdAndType(String userId, UserTwoFAType type);

    UserTwoFAKeyDO getById(String id);

}
