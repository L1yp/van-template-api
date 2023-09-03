package org.cloud.web.context;

import cn.dev33.satoken.stp.StpUtil;
import org.cloud.web.model.DO.system.UserDO;
import org.springframework.web.context.request.RequestContextHolder;

public class LoginUtils {

    public static String getLoginUserId() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            String tokenValue = StpUtil.getTokenValue();
            if (tokenValue == null) {
                return null;
            }
            return StpUtil.stpLogic.getLoginIdNotHandle(tokenValue);
        }
        return null;
    }

}
