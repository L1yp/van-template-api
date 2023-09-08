package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.AbstractOutputDTO;
import org.cloud.web.model.DO.system.UserOperateLogDO;

@Getter
@Setter
@Schema(description = "操作记录对象")
public class UserOperateLogOutputDTO extends AbstractOutputDTO<UserOperateLogDO> {

    @Schema(description = "请求ID")
    private String requestId;


    @Schema(description = "对象类型")
    private String objectType;

    @Schema(description = "操作描述")
    private String remark;

    @Schema(description = "请求方式")
    private String method;

    @Schema(description = "请求地址")
    private String url;

    @Schema(description = "请求参数")
    private String param;

    @Schema(description = "请求耗时")
    private Long duration;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "请求IP")
    private String requestIp;

    @Schema(description = "请求UA")
    private String userAgent;

    @Schema(description = "响应码")
    private Integer responseCode;

}
