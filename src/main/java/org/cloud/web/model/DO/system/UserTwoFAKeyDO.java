package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractModel;
import org.cloud.model.enums.UserTwoFAType;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.web.model.DTO.out.system.UserTwoFAKeyOutputDTO;

@Getter
@Setter
@Table(value = "user_2fa_key", autoResultMap = true)
public class UserTwoFAKeyDO extends AbstractModel<UserTwoFAKeyOutputDTO> {

    @Column(jdbcType = JdbcType.BIGINT)
    private String userId;

    @Column(jdbcType = JdbcType.INTEGER, typeHandler = BasicEnumTypeHandler.class)
    private UserTwoFAType type;

    private String secretKey;

    private Boolean bound;

}
