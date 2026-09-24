package com.obigo.microev.tms.api.presentation.delivery;

import com.obigo.microev.tms.core.domain.validator.common.ValidCommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProcessCompletedReqDto {
    @NotBlank(message = "{validation.api.delivery.shootingImpossibleYn.notBlank}")
    @Schema(description = "촬영불가여부", example = "Y,N")
    private String shootingImpossibleYn;

    @ValidCommonCode(groupCode = "ShootingImpossibleReason")
    @Schema(description = "촬영불가사유[촬영불가여부 Y인 경우 필수]", example = "BROKEN_CAMERA, SERVER_ERROR, ETC")
    private String shootingImpossibleReasonCd;

    @ValidCommonCode(groupCode = "ConsignmentLocation")
    @NotBlank(message = "{validation.api.delivery.consignmentLocationCd.notBlank}")
    @Schema(description = "위탁장소", example = "FRONT_OF_DOOR, SECURITY_OFFICE, DELIVERY_BOX, ETC")
    private String consignmentLocationCd;

}
