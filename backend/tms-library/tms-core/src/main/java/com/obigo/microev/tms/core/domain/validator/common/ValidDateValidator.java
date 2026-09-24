package com.obigo.microev.tms.core.domain.validator.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    private String message;
    private String pattern;
    private DateTimeFormatter dateFormatter;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        message = constraintAnnotation.message();
        pattern = constraintAnnotation.pattern();
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (dateStr == null || dateStr.isEmpty()) {
            return true;
        }
        try {
            LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
