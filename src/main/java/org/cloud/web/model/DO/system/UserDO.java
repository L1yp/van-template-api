package org.cloud.web.model.DO.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractWithUpdateModel;
import org.cloud.model.enums.CommonStatus;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.web.model.DTO.out.system.UserOutputDTO;


@Getter
@Setter
@Table(value = "sys_user", excludeFields = "createBy")
public class UserDO extends AbstractWithUpdateModel<UserOutputDTO> {

    private String username;

    private String nickname;

    private String nicknamePinyin;

    private String password;

    @Column(jdbcType = JdbcType.BIGINT)
    private String deptId;

    private String phone;

    private String email;

    private String avatar;

    private String registerIp;

    private String parentId;

    @Column(typeHandler = BasicEnumTypeHandler.class)
    private CommonStatus status;

    @JsonIgnore
    public boolean isValid() {
        return status == CommonStatus.ENABLE;
    }

}
