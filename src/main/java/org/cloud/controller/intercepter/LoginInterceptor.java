package org.cloud.controller.intercepter;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    public static final String LOGIN_USER_TOKEN = "LOGIN_USER_TOKEN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUserId = StpUtil.getLoginIdDefaultNull();
        Object token = StpUtil.getTokenValueNotCut();
        request.setAttribute(LOGIN_USER_ID, loginUserId);
        request.setAttribute(LOGIN_USER_TOKEN, token);
        return true;
    }
}
