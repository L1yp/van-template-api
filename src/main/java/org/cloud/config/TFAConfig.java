package org.cloud.config;

import lombok.Data;
import org.cloud.model.enums.UserThirdAuthType;
import org.cloud.model.enums.UserTwoFAType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "auth.two-fa")
public class TFAConfig {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 应用名称
     */
    private String issuer;

    private UserTwoFAType type;

}
