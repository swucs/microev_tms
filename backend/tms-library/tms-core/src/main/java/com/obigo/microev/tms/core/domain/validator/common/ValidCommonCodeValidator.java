package com.obigo.microev.tms.core.domain.validator.common;


import com.obigo.microev.tms.core.domain.mapper.CommonCodeMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.ValidCommonCodesResult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * String으로 들어온 값이 Enum Class에 존재하는 타입인지 확인
 */
@Component
@RequiredArgsConstructor
public class ValidCommonCodeValidator implements ConstraintValidator<ValidCommonCode, String> {

    private final CommonCodeMapper commonCodeMapper;

    private String message;
    private String groupCode;
    private final MessageSource messageSource;

    @Override
    public void initialize(ValidCommonCode constraintAnnotation) {
        message = constraintAnnotation.message();
        groupCode = constraintAnnotation.groupCode();
    }

    @Override
    public boolean isValid(String comCodeCd, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (comCodeCd == null || comCodeCd.isEmpty()) {
            return true;
        }

        //메시지가 비어있는 경우 Class의 이름을 메시지로 설정
        if (message.isBlank()) {
            message = messageSource.getMessage("validation.common.invalid.commonCode", new Object[]{groupCode, comCodeCd}, LocaleContextHolder.getLocale());
        }

        List<ValidCommonCodesResult> codes = commonCodeMapper.findValidCodes(groupCode, comCodeCd);
        if (CollectionUtils.isEmpty(codes)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
