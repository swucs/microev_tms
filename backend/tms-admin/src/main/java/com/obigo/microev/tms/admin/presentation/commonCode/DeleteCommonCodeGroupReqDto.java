package com.obigo.microev.tms.admin.presentation.commonCode;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DeleteCommonCodeGroupReqDto {

    @Schema(description = "공통코드 그룹코드 목록")
    @NotEmpty(message = "{validation.commonCode.comCodeGroupName.notBlank}")
    private List<String> comCodeGroupCds;
}