package com.obigo.microev.tms.admin.presentation.commonCode;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifyCommonCodeGroupReqDto {

    @Schema(description = "공통코드 그룹코드")
    @NotBlank(message = "{validation.commonCode.comCodeGroupCd.notBlank}")
    private String comCodeGroupCd;

    @Schema(description = "공통코드 그룹명")
    @NotBlank(message = "{validation.commonCode.comCodeGroupName.notBlank}")
    private String comCodeGroupName;
}