package com.obigo.microev.tms.admin.presentation.driver;


import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.DriverService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "기사관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    @Operation(summary = "배송기사 목록 조회", description = "배송기사 목록을 검색하고 조회한다.")
    public ResponseEntity<ResponseDto<List<SearchDriversResDto>>> searchDrivers(
            @ModelAttribute @Validated @ParameterObject SearchDriversReqDto reqDto
    ) {

        List<SearchDriversResDto> data = driverService.searchDrivers(reqDto);
        return ResponseEntity.ok(ResponseDto.<List<SearchDriversResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build());
    }


    @PostMapping
    @Operation(summary = "배송기사 등록", description = "배송기사를 등록한다.")
    public ResponseEntity<ResponseDto<Long>> createDriver(@RequestBody @Validated CreateDriverReqDto reqDto) {

        long driverSeq = driverService.createDriver(reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(driverSeq)
                .build());
    }


    @PutMapping("/{driverSeq}")
    @Operation(summary = "배송기사 수정", description = "배송기사 정보를 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyDriver(
            @PathVariable long driverSeq,
            @RequestBody @Validated ModifyDriverReqDto reqDto
    ) {

        driverService.modifyDriver(driverSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    @PutMapping("/{driverSeq}/password")
    @Operation(summary = "배송기사 비밀번호 수정", description = "배송기사의 비밀번호를 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyDriverPassword(
            @PathVariable long driverSeq,
            @RequestBody @Validated ModifyDriverPasswordReqDto reqDto
    ) {

        driverService.modifyDriverPassword(driverSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    @DeleteMapping("/{driverSeq}")
    @Operation(summary = "배송기사 삭제", description = "배송기사를 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> removeDriver(@PathVariable long driverSeq) {
        driverService.removeDriver(driverSeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }

}
