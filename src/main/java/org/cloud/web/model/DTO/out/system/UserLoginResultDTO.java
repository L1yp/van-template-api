package org.cloud.web.model.DTO.out.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "登录结果响应")
public class UserLoginResultDTO {

    @Schema(description = "token")
    private String token;

    @Schema(description = "用户信息")
    private UserOutputDTO userInfo;

    @Schema(description = "菜单列表")
    private List<MenuOutputDTO> menuList;

    @Schema(description = "角色ID列表")
    private List<String> roleIdList;

    @Schema(description = "权限列表")
    private List<String> permKeyList;



}
