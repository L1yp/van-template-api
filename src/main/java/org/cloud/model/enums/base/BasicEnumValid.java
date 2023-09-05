package org.cloud.model.enums.base;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Constraint(validatedBy = { BasicEnumIntegerValidation.class })
public @interface BasicEnumValid {


    Class<? extends BasicEnum> value();

    String message() default "Invalid enum value";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
