package com.obigo.microev.tms.admin.presentation.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyAdminReqDto {
    
    @Schema(description = "비밀번호")
    @NotBlank(message = "{validation.admin.password.notBlank}")
    private String password;

    @Schema(description = "관리자명")
    @NotBlank(message = "{validation.admin.adminName.notBlank}")
    private String adminName;

    @Schema(description = "연락처")
    @NotBlank(message = "{validation.admin.contact.notBlank}")
    private String contact;

    @Schema(description = "직급/직책")
    private String position;

    @Schema(description = "상태")
    @NotBlank(message = "{validation.admin.statusCd.notBlank}")
    private String statusCd;
}
