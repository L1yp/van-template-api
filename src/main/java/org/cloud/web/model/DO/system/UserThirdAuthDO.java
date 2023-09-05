package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractModel;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.UserThirdAuthType;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.web.model.DTO.out.system.UserThirdAuthOutputDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "user_third_auth", autoResultMap = true)
public class UserThirdAuthDO extends AbstractModel<UserThirdAuthOutputDTO> {

    @Column(jdbcType = JdbcType.BIGINT)
    private String userId;

    @Column(typeHandler = BasicEnumTypeHandler.class)
    private UserThirdAuthType type;

    private String openId;

    private String unionId;

    private String accessToken;

    private String avatarUrl;

    private String nickname;

    private String lastLoginIp;

    private LocalDateTime lastLoginTime;

    @Column(typeHandler = BasicEnumTypeHandler.class)
    private CommonStatus status;

}
