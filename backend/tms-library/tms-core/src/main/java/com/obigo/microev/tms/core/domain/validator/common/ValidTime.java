package com.obigo.microev.tms.core.domain.validator.common;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTimeValidator.class)
public @interface ValidTime {
    String message() default "{validation.common.notValid.time}";
    String pattern() default "HH:mm";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
