package com.obigo.microev.tms.core.domain.validator.common;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidSizeValidator.class)
public @interface ValidSize {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "{validation.common.ValidSize}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
