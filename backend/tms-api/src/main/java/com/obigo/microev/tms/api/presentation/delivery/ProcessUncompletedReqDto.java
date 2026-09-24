package com.obigo.microev.tms.api.presentation.delivery;

import com.obigo.microev.tms.core.domain.validator.common.ValidCommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProcessUncompletedReqDto {
    @ValidCommonCode(groupCode = "UncompletedReason")
    @Schema(description = "미완료(미배송, 미수거)사유", example = "EXCESSIVE_QUANTITY, CUSTOMER_REQUEST, NO_ENTRY_BUILDING, LOST_PARCEL, ETC")
    private String uncompletedReasonCd;
}
