package org.cloud.controller.intercepter.annotaions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * controller日志标记
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Log {

    /**
     * 支持SpEL表达式, 参数使用#p0, #p1等访问
     * @return 表达式内容
     */
    String value() default "";


    /**
     * 目标对象
     */
    String objectType() default "";

}
