package org.cloud.web.model.DO.system;

import io.mybatis.provider.Entity.Table;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractModel;
import org.cloud.web.model.DTO.out.system.UserOperateLogOutputDTO;

@Getter
@Setter
@Table(value = "user_operate_log", autoResultMap = true)
public class UserOperateLogDO extends AbstractModel<UserOperateLogOutputDTO> {

    private String requestId;

    private String objectType;

    private String remark;

    private String method;

    private String url;

    private String param;

    private Long duration;

    private String errorMessage;

    private String requestIp;

    private String userAgent;

}
