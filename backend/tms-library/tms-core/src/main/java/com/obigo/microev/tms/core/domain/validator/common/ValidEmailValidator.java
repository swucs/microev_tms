package com.obigo.microev.tms.core.domain.validator.common;


import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 이메일 형식 체크하는 Validator
 */
@Component
@RequiredArgsConstructor
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_REGEX =
                    "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private String message;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if(StringUtils.isNotBlank(value)) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            if (!pattern.matcher(value).matches()) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
