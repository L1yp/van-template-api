package org.cloud.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractUpdateDTO<T> implements Converter<T> {

    @NotNull
    @Schema(description = "id", requiredMode = RequiredMode.REQUIRED)
    private String id;

}
