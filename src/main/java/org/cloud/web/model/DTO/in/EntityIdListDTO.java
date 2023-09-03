package org.cloud.web.model.DTO.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "id列表参数")
public class EntityIdListDTO {

    @NotNull
    @Schema(description = "id列表")
    private List<String> idList;

}
