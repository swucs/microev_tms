package com.obigo.microev.tms.core.domain.validator.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * serviceType 이 존재하는지 확인하는 Validator
 */
@Component
@RequiredArgsConstructor
public class ValidSizeValidator implements ConstraintValidator<ValidSize, String> {

    private String message;
    private int max;
    private int min;

    @Override
    public void initialize(ValidSize constraintAnnotation) {
        message = constraintAnnotation.message();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (str == null || str.isEmpty()) {
            return true;
        }

        if (str.length() < min || str.length() > max) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
