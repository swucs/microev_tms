package com.obigo.microev.tms.admin.presentation.login;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 토큰 응답")
public class TokenDto {
    @Schema(description = "토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb255YW4xMUBvYmlnby5jb20iLCJpYXQiOjE3MDA3OTY3NTgsImV4cCI6MTcwMDgwMDM1OH0.rOyGwI63qi8DnXIFAtyStRsas0pMsnzLSdWT8bhAOKs")
    private String accessToken;

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb255YW4xMUBvYmlnby5jb20iLCJpYXQiOjE3MDA3OTY3NTgsImV4cCI6MTcwMjAwNjM1OH0.r5HlEajAXgdaNYuW8o5usCStPVkU1HkH3SJVD9HtKoY")
    private String refreshToken;
}
