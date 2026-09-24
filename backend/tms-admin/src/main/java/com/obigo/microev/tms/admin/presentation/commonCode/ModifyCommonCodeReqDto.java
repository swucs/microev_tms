package com.obigo.microev.tms.admin.presentation.commonCode;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifyCommonCodeReqDto {
    @Schema(description = "공통코드 코드")
    @NotBlank(message = "{validation.commonCode.comCodeCd.notBlank}")
    @Size(min = 2, message = "{validation.commonCode.comCodeCd.minLength}")
    private String comCodeCd;

    @Schema(description = "공통코드 명")
    @NotBlank(message = "{validation.commonCode.comCodeName.notBlank}")
    @Size(min = 2, message = "{validation.commonCode.comCodeName.minLength}")
    private String comCodeName;

    @Schema(description = "정렬순서")
    @NotNull(message = "{validation.commonCode.sortOrder.notNull}")
    private Integer sortOrder;

    @Schema(description = "사용여부", allowableValues = {"Y", "N"})
    @NotBlank(message = "{validation.commonCode.usageYn.notBlank}")
    @Pattern(regexp = "Y|N", message = "{validation.commonCode.usageYn.notBlank}")
    private String usageYn;

    @Schema(description = "속성1")
    private String attribute1;

    @Schema(description = "속성2")
    private String attribute2;

    @Schema(description = "속성3")
    private String attribute3;

}