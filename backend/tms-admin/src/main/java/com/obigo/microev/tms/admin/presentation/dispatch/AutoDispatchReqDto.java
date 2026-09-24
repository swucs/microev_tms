package com.obigo.microev.tms.admin.presentation.dispatch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AutoDispatchReqDto {

    @Schema(description = "센터시퀀스")
    @NotNull(message = "{validation.dispatch.centerSeq.notNull}")
    private Long centerSeq;
    
    @Schema(description = "도로명 주소")
    @NotBlank(message = "{validation.dispatch.roadAddress.notBlank}")
    private String roadAddress;
}
