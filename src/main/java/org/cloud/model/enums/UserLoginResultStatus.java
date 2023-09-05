package org.cloud.model.enums;

import org.cloud.model.enums.base.BasicEnum;

public enum UserLoginResultStatus implements BasicEnum {
    SUCCESS(0, "成功"),
    AUTHENTICATOR_UNBOUND(1, "未绑定Authenticator"),
    MISS_TFA_CODE(2, "未输入Authenticator验证码"),
    ;

    private final int value;

    private final String description;

    UserLoginResultStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String getKey() {
        return "user.login.result.type";
    }
}
