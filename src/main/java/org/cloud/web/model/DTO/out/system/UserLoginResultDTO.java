package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.enums.UserLoginResultStatus;
import org.cloud.model.enums.base.BasicEnumValid;

import java.util.List;

@Getter
@Setter
@Schema(description = "登录结果响应")
public class UserLoginResultDTO {

    @Schema(description = "token")
    private String token;

    @NotNull
    @BasicEnumValid(UserLoginResultStatus.class)
    @Schema(description = "登录结果")
    private Integer status;

    @Schema(description = "用户信息")
    private UserOutputDTO userInfo;

    @Schema(description = "菜单列表")
    private List<MenuOutputDTO> menuList;

    @Schema(description = "角色ID列表")
    private List<String> roleIdList;

    @Schema(description = "权限列表")
    private List<String> permKeyList;

    @Schema(description = "绑定信息")
    private AuthBindInfo bindInfo;

    @Getter
    @Setter
    @AllArgsConstructor
    @Schema(description = "授权信息")
    public static class AuthBindInfo {
        @Schema(description = "授权的URL")
        private String otpAuthURL;
    }

}
