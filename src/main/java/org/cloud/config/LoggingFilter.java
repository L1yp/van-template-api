package org.cloud.config;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cloud.controller.intercepter.LoginInterceptor;
import org.cloud.controller.intercepter.RequestUniqueIdInterceptor;
import org.cloud.controller.intercepter.task.OperateLogStorageTask;
import org.cloud.web.model.DO.system.UserOperateLogDO;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ServletRequestPathUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

    @Resource
    RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    ThreadPoolTaskScheduler taskScheduler;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        ServletRequestPathUtils.parseAndCache(request);
        Map<RequestMappingInfo, HandlerMethod> handlerMapping = this.requestMappingHandlerMapping.getHandlerMethods();
        return handlerMapping.keySet().stream().allMatch(info -> info.getMatchingCondition(request) == null);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        long startTime = System.nanoTime();
        filterChain.doFilter(requestWrapper, response);
        long takeNanos = System.nanoTime() - startTime;

        String objectType = null;
        String summary = null;
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handlerMethod != null) {
            Class<?> beanType = handlerMethod.getBeanType();
            AnnotationAttributes mergedAnnotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(beanType, "io.swagger.v3.oas.annotations.tags.Tag");
            if (mergedAnnotationAttributes != null) {
                objectType = mergedAnnotationAttributes.getString("name");
            }
            if (objectType == null) {
                return;
            }

            Method method = handlerMethod.getMethod();
            mergedAnnotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(method, "io.swagger.v3.oas.annotations.Operation");
            if (mergedAnnotationAttributes != null) {
                summary = mergedAnnotationAttributes.getString("summary");
            }
        }


        String param = null;
        byte[] bytesBody = requestWrapper.getContentAsByteArray();
        String characterEncoding = request.getCharacterEncoding();
        if (bytesBody.length > 0) {
            param = new String(bytesBody, characterEncoding);
        }

        String requestId = response.getHeader(RequestUniqueIdInterceptor.REQUEST_ID_HEADER_KEY);
        String method = request.getMethod();
        String url = request.getRequestURI();


        long duration = Duration.ofNanos(takeNanos).toMillis();

        // error message
        // error message
        String errorMsg = null;
        Object exp = request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
        if (exp instanceof Throwable e) {
            errorMsg = e.getMessage();
            log.warn("{}", errorMsg);
        }

        String requestIp = request.getHeader("X-Real-IP");
        requestIp = requestIp == null ? request.getRemoteAddr() : requestIp;

        String userAgent = request.getHeader("User-Agent");

        String token = (String) request.getAttribute(LoginInterceptor.LOGIN_USER_TOKEN);

        var model = new UserOperateLogDO();
        model.setRequestId(requestId);
        model.setObjectType(objectType);
        model.setRemark(summary);
        model.setMethod(method);
        model.setUrl(url);
        model.setParam(param);
        model.setDuration(duration);
        model.setErrorMessage(errorMsg);
        model.setRequestIp(requestIp);
        model.setUserAgent(userAgent);
        model.setToken(token);
        taskScheduler.execute(new OperateLogStorageTask(model));

    }
}
