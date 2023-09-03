package org.cloud.web.model.DTO.out.system;

import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.web.model.DO.system.UserLoginLogDO;

@Getter
@Setter
public class UserLoginLogOutputDTO extends AbstractOutputDTO<UserLoginLogDO> {

    private String username;
    private String nickname;
    private Integer loginType;
    private String loginIp;

}
