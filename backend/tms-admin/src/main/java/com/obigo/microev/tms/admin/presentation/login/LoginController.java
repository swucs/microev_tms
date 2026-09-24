package com.obigo.microev.tms.admin.presentation.login;


import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.LoginService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 관련")
@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @PutMapping("/newToken")
    @Operation(summary = "신규 토큰 발급", description = "accessToken 만료시에 새로운 토큰 발급한다. [유효한 refreshToken이 있어야 한다]")
    public ResponseEntity<ResponseDto<ReissueTokenResDto>> reissueToken(@RequestBody @Validated ReissueTokenReqDto req) {
        ReissueTokenResDto resDto = loginService.reissueToken(req);

        ResponseCode successCode = ResponseCode.SUCCESS;
        return ResponseEntity.ok(ResponseDto.<ReissueTokenResDto>builder()
                .resultCode(successCode.getCode())
                .resultMessage(successCode.getMessage())
                .data(resDto)
                .build());
    }
}
