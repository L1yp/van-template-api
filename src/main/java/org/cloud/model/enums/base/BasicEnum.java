package org.cloud.model.enums.base;

import com.fasterxml.jackson.annotation.JsonValue;

public interface BasicEnum {

    @JsonValue
    int getValue();

    String description();

    String getKey();

}
