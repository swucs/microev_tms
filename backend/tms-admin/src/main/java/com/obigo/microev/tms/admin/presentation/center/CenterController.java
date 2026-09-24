package com.obigo.microev.tms.admin.presentation.center;


import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.CenterService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "센터관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/center")
public class CenterController {

    private final CenterService centerService;

    @GetMapping
    @Operation(summary = "센터 목록 조회", description = "센터 목록을 검색하고 조회한다.")
    public ResponseEntity<ResponseDto<List<SearchCentersResDto>>> searchCenters(
            @ModelAttribute @Validated @ParameterObject SearchCentersReqDto reqDto
    ) {

        List<SearchCentersResDto> data = centerService.searchCenters(reqDto);
        return ResponseEntity.ok(ResponseDto.<List<SearchCentersResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build());
    }

    @PostMapping
    @Operation(summary = "센터 등록", description = "센터를 등록한다.")
    public ResponseEntity<ResponseDto<Long>> createCenter(@RequestBody @Validated CreateCenterReqDto reqDto) {

        long centerSeq = centerService.createCenter(reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(centerSeq)
                .build());
    }

    @GetMapping("/{centerSeq}")
    @Operation(summary = "센터 상세 조회", description = "센터를 상세 조회한다.")
    public ResponseEntity<ResponseDto<CenterDetailResDto>> getCenter(@PathVariable Long centerSeq) {
        CenterDetailResDto resDto = centerService.getCenterDetail(centerSeq);

        return ResponseEntity.ok(ResponseDto.<CenterDetailResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

    @PutMapping("/{centerSeq}")
    @Operation(summary = "센터 수정", description = "센터를 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyCenter(
            @PathVariable Long centerSeq,
            @RequestBody @Validated ModifyCenterReqDto reqDto
    ) {
        centerService.modifyCenter(centerSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }

    @DeleteMapping("/{centerSeq}")
    @Operation(summary = "센터 삭제", description = "센터를 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> removeCenter(@PathVariable Long centerSeq) {
        centerService.removeCenter(centerSeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    @GetMapping("/{centerSeq}/vehicle")
    @Operation(summary = "차량운전자 조회", description = "센터 시퀀스를 입력받아 차량운전자를 조회한다.")
    public ResponseEntity<ResponseDto<List<GetVehicleDriverResDto>>> getVehiclesByCenterSeq(@PathVariable("centerSeq") Long centerSeq) throws Exception {
        List<GetVehicleDriverResDto> resDtos = centerService.getVehiclesByCenterSeq(centerSeq);

        return ResponseEntity.ok(ResponseDto.<List<GetVehicleDriverResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDtos)
                .build());
    }
}
