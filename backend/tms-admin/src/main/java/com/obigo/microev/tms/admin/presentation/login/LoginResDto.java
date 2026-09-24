package com.obigo.microev.tms.admin.presentation.login;

import com.obigo.microev.tms.core.domain.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LoginResDto {
    private Long portalUserSeq;
    private String email;
    private String adminName;
    private String accessToken;
    private String refreshToken;

    public static LoginResDto of(Admin admin, String newAccessToken, String newRefreshToken) {
        return LoginResDto.builder()
                .portalUserSeq(admin.getAdminSeq())
                .email(admin.getEmail())
                .adminName(admin.getAdminName())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
