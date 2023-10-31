package org.cloud.web.context;

import org.cloud.controller.intercepter.LoginInterceptor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LoginUtils {

    public static String getLoginUserId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return (String) requestAttributes.getRequest().getAttribute(LoginInterceptor.LOGIN_USER_ID);
        }
        return null;
    }

    public static void setLoginUserId(String loginUserId) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.getRequest().setAttribute(LoginInterceptor.LOGIN_USER_ID, loginUserId);
        }
    }

}
