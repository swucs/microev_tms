package com.obigo.microev.tms.admin.presentation.vehicle;


import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.VehicleService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "차량관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "차량 목록 조회", description = "차량 목록을 조회한다.")
    public ResponseEntity<ResponseDto<List<SearchVehiclesResDto>>> searchVehicles(
            @ModelAttribute @Validated @ParameterObject SearchVehiclesReqDto reqDto
    ) {
        List<SearchVehiclesResDto> data = vehicleService.searchVehicles(reqDto);

        return ResponseEntity.ok(ResponseDto.<List<SearchVehiclesResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build());
    }

    @PostMapping
    @Operation(summary = "차량 등록", description = "차량정보를 등록한다.")
    public ResponseEntity<ResponseDto<Long>> createVehicle(
            @RequestBody @Validated CreateVehicleReqDto createVehicleReqDto
    ) {
        Long newVehicleSeq = vehicleService.createVehicle(createVehicleReqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(newVehicleSeq)
                .build());
    }
    
    @PutMapping("/{vehicleSeq}")
    @Operation(summary = "차량 수정", description = "차량정보를 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyVehicle(
            @PathVariable Long vehicleSeq,
            @RequestBody @Validated ModifyVehicleReqDto modifyVehicleReqDto
    ) {
        vehicleService.modifyVehicle(vehicleSeq, modifyVehicleReqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }

    @DeleteMapping("/{vehicleSeq}")
    @Operation(summary = "차량 삭제", description = "차량정보를 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> deleteVehicle(
            @PathVariable Long vehicleSeq
    ) {
        vehicleService.deleteVehicle(vehicleSeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    
}
