package com.obigo.microev.tms.admin.presentation.common;

import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.CommonService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공통")
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/coordinate")
    @Operation(summary = "좌표 구하기", description = "주소를 입력받아 좌표를 구한다.")
    public ResponseEntity<ResponseDto<GetCoordinateResDto>> getCoordinate(@ModelAttribute @Validated @ParameterObject GetCoordinateReqDto reqDto) throws Exception {
        GetCoordinateResDto resDto = commonService.getCoordinate(reqDto);

        return ResponseEntity.ok(ResponseDto.<GetCoordinateResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }


    @GetMapping("/eupMyeonDong")
    @Operation(summary = "읍면동 조회", description = "주소를 입력받아 읍면동을 조회한다.")
    public ResponseEntity<ResponseDto<GetEupMyeonDongResDto>> getEupMyeonDong(@ModelAttribute @Validated @ParameterObject GetEupMyeonDongReqDto reqDto) throws Exception {
        GetEupMyeonDongResDto resDto = commonService.getEupMyeonDong(reqDto);

        return ResponseEntity.ok(ResponseDto.<GetEupMyeonDongResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }
}
