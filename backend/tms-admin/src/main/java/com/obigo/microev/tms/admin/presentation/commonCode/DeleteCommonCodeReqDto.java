package com.obigo.microev.tms.admin.presentation.commonCode;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DeleteCommonCodeReqDto {
    @Schema(description = "공통코드 시퀀스 목록")
    @NotEmpty(message = "{validation.commonCode.comCodeSeq.notNull}")
    private List<Long> comCodeSeqs;
}