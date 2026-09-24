package com.obigo.microev.tms.api.presentation.authentication;


import com.obigo.microev.tms.api.presentation.ResponseDto;
import com.obigo.microev.tms.api.service.AuthenticationService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "인증관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "차량 로그인 처리", description = "차량 email/pin을 입력받아 로그인 처리하고 토큰을 발급한다.")
    public ResponseEntity<ResponseDto<DriverLoginResDto>> login(@RequestBody @Validated DriverLoginReqDto reqDto) {

        DriverLoginResDto resDto = authenticationService.login(reqDto);

        return ResponseEntity.ok(ResponseDto.<DriverLoginResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }


    @PostMapping("/logout")
    @Operation(summary = "차량 로그아웃 처리", description = "차량 로그아웃 처리하고 토큰을 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> logout() {

        authenticationService.logout();

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }

    @GetMapping("/mobile-token")
    @Operation(summary = "모바일용 토큰을 반환한다.", description = "모바일용 토큰을 반환한다.")
    public ResponseEntity<ResponseDto<DriverLoginResDto>> mobileToken() {
        DriverLoginResDto resDto = authenticationService.getTokenForMobile();

        return ResponseEntity.ok(ResponseDto.<DriverLoginResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

}
