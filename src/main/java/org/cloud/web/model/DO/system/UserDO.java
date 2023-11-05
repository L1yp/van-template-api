package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.cache.ICachePutOperation;
import org.cloud.model.AbstractWithUpdateModel;
import org.cloud.model.enums.CommonStatus;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.web.model.DTO.out.system.UserOutputDTO;

import java.beans.Transient;
import java.util.Collection;


@Getter
@Setter
@Table(value = "sys_user", excludeFields = "createBy")
public class UserDO extends AbstractWithUpdateModel<UserOutputDTO> implements ICachePutOperation {

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

    @Transient
    public boolean isValid() {
        return status == CommonStatus.ENABLE;
    }

    @Getter(AccessLevel.NONE)
    private transient Collection<String> cacheKeys;

    private transient boolean cacheHit = true;

    @Override
    public Collection<String> genKeys() {
        return cacheKeys;
    }

    @Transient
    public void setCacheHit(boolean cacheHit) {
        this.cacheHit = cacheHit;
    }

    @Override
    @Transient
    public boolean isCacheHit() {
        return false;
    }
}
