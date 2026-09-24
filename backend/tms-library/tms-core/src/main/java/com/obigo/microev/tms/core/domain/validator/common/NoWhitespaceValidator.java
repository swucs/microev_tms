package com.obigo.microev.tms.core.domain.validator.common;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 문자 중간에 공백이 있는지 확인하는 Validator
 */
@Component
@RequiredArgsConstructor
public class NoWhitespaceValidator implements ConstraintValidator<NoWhitespace, String> {

    private String message;

    @Override
    public void initialize(NoWhitespace constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if(str != null) {
        boolean contains = str.contains(" ");
            if (contains) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
