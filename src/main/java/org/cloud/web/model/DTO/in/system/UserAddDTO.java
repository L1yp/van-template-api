package org.cloud.web.model.DTO.in.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.cloud.model.Converter;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.base.BasicEnumValid;
import org.cloud.web.model.DO.system.UserDO;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@Schema(description = "用户注册类型")
public class UserAddDTO implements Converter<UserDO> {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z_$][\\w$]*$", message = "用户名必须字母或下划线开头")
    @Schema(description = "用户名", example = "Lyp")
    private String username;


    @NotBlank
    @Schema(description = "昵称", example = "韩信")
    private String nickname;

    @Length(min = 32, max = 32)
    @NotBlank
    @Schema(description = "用户密码", example = "123456")
    private String password;

    @Schema(description = "推荐人Id")
    private String parentId;

    @Schema(description = "部门ID")
    private String deptId;

    @Schema(description = "兼职部门ID列表")
    private List<String> deptIdList;

    @Schema(description = "角色ID列表")
    private List<String> roleIdList;


    @Schema(description = "手机号", example = "13145205200")
    private String phone;

    @Schema(description = "邮箱", example = "l1yp@qq.com")
    private String email;

    @Schema(description = "头像", example = "https://q4.qlogo.cn/g?b=qq&nk=942664114&s=0")
    private String avatar;

    @JsonIgnore
    @Schema(description = "注册IP", example = "127.0.0.1", hidden = true)
    private String registerIp;

    @NotNull
    @BasicEnumValid(CommonStatus.class)
    @Schema(description = "状态", example = "0")
    private Integer status;

}
