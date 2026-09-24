package com.obigo.microev.tms.admin.presentation.dispatch;

import com.obigo.microev.tms.core.domain.validator.common.ValidDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDispatchReqDto {
    
    @Schema(description = "센터시퀀스")
    @NotNull(message = "{validation.dispatch.centerSeq.notNull}")
    private Long centerSeq;

    @Schema(description = "배차명")
    @NotBlank(message = "{validation.dispatch.dispatchName.notBlank}")
    private String dispatchName;

    @Schema(description = "배송일자")
    @NotBlank(message = "{validation.dispatch.deliveryDate.notBlank}")
    @ValidDate
    private String deliveryDate;
}
