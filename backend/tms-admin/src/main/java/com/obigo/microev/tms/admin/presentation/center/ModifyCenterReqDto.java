package com.obigo.microev.tms.admin.presentation.center;

import com.obigo.microev.tms.core.domain.validator.common.ValidCommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModifyCenterReqDto {
    
    @Schema(description = "센터명", example = "판교센터")
    @NotBlank(message = "{validation.center.centerName.notBlank}")
    private String centerName;

    @Schema(description = "센터유형코드")
    @NotBlank(message = "{validation.center.centerTypeCd.notBlank}")
    @ValidCommonCode(groupCode = "CenterType")
    private String centerTypeCd;

    @Schema(description = "우편번호", example = "12345")
    @NotBlank(message = "{validation.center.zipCode.notBlank}")
    private String zipCode;

    @Schema(description = "주소")
    @NotBlank(message = "{validation.center.centerAddr1.notBlank}")
    private String centerAddr1;

    @Schema(description = "상세주소")
    @NotBlank(message = "{validation.center.centerAddr2.notBlank}")
    private String centerAddr2;

    @Schema(description = "위도")
    @NotNull(message = "{validation.center.latitude.notNull}")
    private String latitude;

    @Schema(description = "경도")
    @NotNull(message = "{validation.center.longitude.notNull}")
    private String longitude;

    @Schema(description = "총면적")
    private String totalArea;

    @Schema(description = "관리자명")
    @NotBlank(message = "{validation.center.managerName.notBlank}")
    private String managerName;

    @Schema(description = "연락처")
    @NotBlank(message = "{validation.center.contactNum.notBlank}")
    private String contactNum;

    @Schema(description = "사용여부")
    @NotBlank(message = "{validation.center.usageYn.notBlank}")
    private String usageYn;
}
