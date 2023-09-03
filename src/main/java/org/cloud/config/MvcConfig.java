package org.cloud.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.http.HttpServletRequest;
import org.cloud.controller.intercepter.RequestUniqueIdInterceptor;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * @Author Lyp
 * @Date   2020/7/11
 * @Email  l1yp@qq.com
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public Converter<String, LocalDate> DateConvert() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        };
    }


    @Bean
    public FilterRegistrationBean<RequestWrapperFilter> filterFilterRegistrationBean() {
        var registry = new FilterRegistrationBean<RequestWrapperFilter>();
//        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
//        filter.setIncludeHeaders(true);
//        filter.setIncludePayload(true);
//        filter.setIncludeClientInfo(true);
//        filter.setIncludeQueryString(true);
        registry.setFilter(new RequestWrapperFilter());
        registry.addUrlPatterns("/*");
        registry.setOrder(1);
        registry.setName("RequestWrapperFilter");
        return registry;

    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestUniqueIdInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/resources/**");
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/resources/**");
//        registry.addInterceptor(new OperateRecordInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/static/**")
//                .excludePathPatterns("/resources/**");
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        HttpMessageConverter<?> converter = null;
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i).getClass() == MappingJackson2HttpMessageConverter.class) {
                converter = converters.get(i);
                break;
            }
        }

        /**
         * 用于解决当返回类型是String时优先被StringHttpMessageConverter处理(supportTypes有一个*\/*)，因此将MappingJackson2HttpMessageConverter提到首位
         * 第0位是org.springframework.http.converter.ByteArrayHttpMessageConverter, SpringDoc的/v3/api-docs返回的是byte[]，因此不能把json的处理放到第0位
         * @see OpenApiResource#openapiJson(HttpServletRequest, String, Locale)
         * @see AbstractOpenApiResource#writeYamlValue(OpenAPI)
         */
        converters.remove(converter);
        converters.add(1, converter);
    }
}
