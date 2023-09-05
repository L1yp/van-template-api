package org.cloud.web.model.DO.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.mybatis.provider.Entity.Column;
import io.mybatis.provider.Entity.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.AbstractWithUpdateModel;
import org.cloud.model.enums.CommonStatus;
import org.cloud.model.enums.MenuType;
import org.cloud.mybatis.typehandler.BasicEnumTypeHandler;
import org.cloud.mybatis.typehandler.JacksonTypeHandler;
import org.cloud.web.model.DTO.out.system.MenuOutputDTO;

@Getter
@Setter
@Table(value = "sys_menu", autoResultMap = true)
public class MenuDO extends AbstractWithUpdateModel<MenuOutputDTO> {

    private String name;

    @Column(jdbcType = JdbcType.BIGINT)
    private String pid;

    @Column(typeHandler = BasicEnumTypeHandler.class)
    private MenuType type;

    private String path;

    private String component;

    @Column(typeHandler = JacksonTypeHandler.class)
    private MenuMeta meta;

    private Integer orderNo;


    @Column(typeHandler = BasicEnumTypeHandler.class)
    private CommonStatus status;

    private String remark;


    @Getter
    @Setter
    @Schema(description = "菜单原数据")
    @JsonInclude(Include.NON_NULL)
    public static class MenuMeta {

        private String icon;

        private Boolean closeable;

        private Boolean hidden;

    }


}
