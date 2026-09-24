package com.obigo.microev.tms.core.domain.validator.admin;

import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.exception.BusinessException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        try {
            checkValidPassword(password);
        } catch (BusinessException e) {
            log.info("Exception occurred while validating password: {}", e.getMessage());
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(e.getResponseCode().getMessage())
                    .addConstraintViolation();
            return false;
        } catch (Exception e) {
            log.info("Exception occurred while validating password: {}", e.getMessage());
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(ResponseCode.SERVER_EXCEPTION.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    /**
     * 비밀번호 유효성 체크
     * @param password
     */
    private void checkValidPassword(String password) {
        //비밀번호 자리수가 8자리 인지 아닌지 체크
        if (StringUtils.isBlank(password) || password.length() < 8) {
            throw new BusinessException(ResponseCode.INVALID_PASSWORD_LENGTH);
        }

        // 연속으로 동일한 문자 3번 이상 쓰였는지 체크
        final String sameCharPattern = "(.)\\1\\1";
        Pattern pattern = Pattern.compile(sameCharPattern);
        if (pattern.matcher(password).find()) {
            throw new BusinessException(ResponseCode.INVALID_PASSWORD_CONTAINS_SAME_CHAR);
        }

        //영문, 숫자, 특수문자가 모두 포함되어 있는지 체크
        final String passwordPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&_]+$";
        pattern = Pattern.compile(passwordPattern);
        if (!pattern.matcher(password).matches()) {
            throw new BusinessException(ResponseCode.INVALID_PASSWORD_CONTAINS_SPECIAL_CHAR);
        }
    }
}


