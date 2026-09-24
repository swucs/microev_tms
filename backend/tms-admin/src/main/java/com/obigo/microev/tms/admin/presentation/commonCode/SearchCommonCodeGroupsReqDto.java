package com.obigo.microev.tms.admin.presentation.commonCode;

import com.obigo.microev.tms.core.domain.validator.common.ValidSize;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@Data
@ParameterObject
public class SearchCommonCodeGroupsReqDto {
    @Parameter(description = "공통코드 그룹코드")
    @ValidSize(min = 2, message = "{validation.commonCode.comCodeGroupCd.minLength}")
    private String comCodeGroupCd;

    @Parameter(description = "공통코드 그룹명")
    @ValidSize(min = 2, message = "{validation.commonCode.comCodeGroupName.minLength}")
    private String comCodeGroupName;
}

