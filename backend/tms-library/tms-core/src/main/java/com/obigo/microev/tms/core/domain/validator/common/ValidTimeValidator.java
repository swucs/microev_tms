package com.obigo.microev.tms.core.domain.validator.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidTimeValidator implements ConstraintValidator<ValidTime, String> {

    private String message;
    private DateTimeFormatter dateFormatter;

    @Override
    public void initialize(ValidTime constraintAnnotation) {
        message = constraintAnnotation.message();
        String pattern = constraintAnnotation.pattern();
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public boolean isValid(String timeStr, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (timeStr == null || timeStr.isEmpty()) {
            return true;
        }
        try {
            LocalTime.parse(timeStr, dateFormatter);
        } catch (DateTimeParseException e) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
