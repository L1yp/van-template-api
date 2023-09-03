package org.cloud.config;

import org.cloud.model.common.ResultData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 通用响应格式<br/>
 * Controller的参数有ServletResponse时的处理<br/>
 * {@see org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver#resolveArgument:67}<br/>
 * ServletResponseMethodArgumentResolver 参数解析类把requestHandled(请求已经完全处理)标记为true<br/>
 * {@see org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod:122}<br/>
 * ServletInvocableHandlerMethod中, 若requestHandled为true, 则直接返回, 不继续使用message converter处理。<br/>
 *<br/>
 * Controller返回String时的处理<br/>
 * org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor#writeWithMessageConverters<br/>
 * 1. 读取response header的content-type判断是否预设content-type<br/>
 * 2. 读取request header的accept属性<br/>
 * 3. 读取RequestMapper的produce属性<br/>
 * 最终决定使用哪个mediaType返回的处理org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor#determineCompatibleMediaTypes<br/>
 *<br/>
 * 1. 默认返回String会使用此类处理<br/>
 * 2. 若需要直接返回String, 而不使用通用结构时, 需要在RequestMapping上添加produces="text/plain"<br/>
 */
// 为了保持文档上的一致性，取消统一响应结构处理
//@RestControllerAdvice(basePackages = "org.cloud.web.controller")
//@Order(1)
@Deprecated
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
 
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(SkipResponseAdvice.class)) {
            return false;
        }
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }
 
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof ResultData<?>) {
            return body;
        }

        if (body instanceof String) {
            return ResultData.ok(body);
        }

        if (returnType.getParameterType().equals(void.class) && body == null) {
            return ResultData.OK;
        }

        return ResultData.ok(body);
    }
}