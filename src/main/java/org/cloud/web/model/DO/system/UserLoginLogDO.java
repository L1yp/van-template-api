package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractModel;
import org.cloud.model.enums.LoginType;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.web.model.DTO.out.system.UserLoginLogOutputDTO;

@Getter
@Setter
@Table(value = "user_login_log", autoResultMap = true)
public class UserLoginLogDO extends AbstractModel<UserLoginLogOutputDTO> {

    private String username;
    private String nickname;
    private String token;

    @Column(typeHandler = BasicEnumTypeHandler.class)
    private LoginType loginType;
    private String loginIp;


}
