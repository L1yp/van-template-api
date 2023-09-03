package org.cloud.controller.intercepter.aspect;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.controller.intercepter.RequestUniqueIdInterceptor;
import org.cloud.controller.intercepter.annotaions.Log;
import org.cloud.controller.intercepter.task.OperateLogStorageTask;
import org.cloud.util.SpringContext;
import org.cloud.web.context.LoginUtils;
import org.cloud.web.model.DO.system.UserOperateLogDO;
import org.slf4j.MDC;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 记录请求日志
     */
    @Pointcut("@annotation(org.cloud.controller.intercepter.annotaions.Log) || @annotation(io.swagger.v3.oas.annotations.Operation)")
    public void logPointCut() {}


    @Around("logPointCut()")
    public Object aroundAdvice(JoinPoint joinPoint) throws Throwable {
        Object result = null;
        var model = new UserOperateLogDO();
        boolean record = setOperateLogAttribute(model, joinPoint);

        long startTime = System.nanoTime();
        try {
            result = ((ProceedingJoinPoint) joinPoint).proceed();
            long duration = System.nanoTime() - startTime;
            model.setDuration(Duration.ofNanos(duration).toMillis());
        } catch (Exception e) {
            long duration = System.nanoTime() - startTime;
            model.setDuration(Duration.ofNanos(duration).toMillis());
            model.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            if (record) {
                ThreadPoolTaskScheduler taskScheduler = SpringContext.getBean(ThreadPoolTaskScheduler.class);
                taskScheduler.execute(new OperateLogStorageTask(model));
            }
        }

        return result;

    }


    private boolean setOperateLogAttribute(UserOperateLogDO model, JoinPoint joinPoint) throws Exception {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return false;
        }
        HandlerMethod handlerMethod = null;
        HttpServletRequest request = null;

        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            request = servletRequestAttributes.getRequest();
            handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
            if (handlerMethod == null) {
                return false;
            }

            model.setRequestId(MDC.get(RequestUniqueIdInterceptor.REQUEST_ID));
            model.setMethod(request.getMethod());
            model.setUrl(request.getRequestURI());
            model.setRequestIp(request.getRemoteAddr());
            model.setCreateBy(LoginUtils.getLoginUserId());
            model.setCreateTime(LocalDateTime.now());
            model.setDuration(0L);
            if (request instanceof ContentCachingRequestWrapper requestWrapper) {
                byte[] requestBody = requestWrapper.getContentAsByteArray();
                String characterEncoding = request.getCharacterEncoding();
                if (requestBody.length > 0) {
                    model.setParam(new String(requestBody, characterEncoding));
                }
            }
        }


        String objectType = null;
        String strExpression = null;
        if (joinPoint.getSignature() instanceof MethodSignature methodSignature && request != null) {
            Method method = methodSignature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);
            // 在实现类写Log注解，此处获取到的是接口的method
            if (logAnnotation == null && method.getDeclaringClass().isInterface()) {
                method = handlerMethod.getMethod();
                logAnnotation = method.getAnnotation(Log.class);
            }
            if (logAnnotation != null) {
                strExpression = logAnnotation.value();
                objectType = logAnnotation.objectType();
                model.setObjectType(objectType);
            }

            if (StringUtils.isNotBlank(strExpression)) {
                // 创建 SpEL 表达式解析器
                ExpressionParser parser = new SpelExpressionParser();

                // 解析表达式
                Expression expression = parser.parseExpression(strExpression);
                Object[] args = joinPoint.getArgs();

                EvaluationContext context = new StandardEvaluationContext();
                for (int i = 0; i < args.length; i++) {
                    context.setVariable("p" + i, args[i]);
                }

                String exprValue = (String) expression.getValue(context);
                model.setRemark(exprValue);
            }

            Class<?> beanType = handlerMethod.getBeanType();
            // 支持继承父级的注解
            Tag tagAnnotation = AnnotatedElementUtils.findMergedAnnotation(beanType, Tag.class);
            // 如果没有Log注解或没有objectType标记, 则尝试取Controller的ObjectType
            if (tagAnnotation != null && StringUtils.isBlank(objectType)) {
                objectType = tagAnnotation.name();
                model.setObjectType(objectType);
            }

            // 如果都取不到, 则取Operation的我summary
            if (StringUtils.isBlank(model.getRemark())) {
                Operation operationAnnotation = null;
                if (method.isAnnotationPresent(Operation.class)) {
                    operationAnnotation = method.getAnnotation(Operation.class);
                }
                if (operationAnnotation == null && handlerMethod.hasMethodAnnotation(Operation.class)) {
                    operationAnnotation = handlerMethod.getMethodAnnotation(Operation.class);
                }

                if (operationAnnotation != null) {
                    // 隐藏时不保存日志
                    if (operationAnnotation.hidden()) {
                        return false;
                    }
                    String summary = operationAnnotation.summary();
                    model.setRemark(summary);
                    if (StringUtils.isBlank(summary)) {
                        String description = operationAnnotation.description();
                        model.setRemark(description);
                    }

                }

            }

        }

        return true;

    }




}
