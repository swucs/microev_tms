package com.obigo.microev.tms.admin.presentation.login;



import com.obigo.microev.tms.core.domain.validator.admin.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyPasswordReqDto {

    @NotBlank(message = "{validation.login.email.notBlank}")
    private String email;

    @NotBlank(message = "{validation.login.password.notBlank}")
    private String currentPassword;

    @ValidPassword
    @NotBlank(message ="{validation.login.newPassword.notBlank}")
    private String newPassword;
}
