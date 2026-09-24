package com.obigo.microev.tms.admin.presentation.region;

import com.obigo.microev.tms.admin.converter.RegionConverter;
import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.RegionService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "권역관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/region")
public class RegionController {
    private final RegionConverter regionConverter;
    private final RegionService regionService;


    @GetMapping
    @Operation(summary = "권역 조회", description = "권역정보를 조회한다.")
    public ResponseEntity<ResponseDto<List<SearchRegionsResDto>>> searchRegions(
            @ModelAttribute @ParameterObject SearchRegionsReqDto reqDto
    ) {
        List<SearchRegionsResDto> resDtos = regionService.searchRegions(reqDto);

        return ResponseEntity.ok(ResponseDto.<List<SearchRegionsResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDtos)
                .build());
    }



    @PostMapping
    @Operation(summary = "권역 등록", description = "권역정보를 등록한다.")
    public ResponseEntity<ResponseDto<Long>> createRegion(
            @RequestBody @Validated CreateRegionReqDto reqDto
    ) {
        long regionSeq = regionService.createRegion(reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(regionSeq)
                .build());
    }

    @GetMapping("/{regionSeq}")
    @Operation(summary = "권역 상세 조회", description = "권역정보를 상세 조회한다.")
    public ResponseEntity<ResponseDto<RegionDetailResDto>> getRegion(@PathVariable Long regionSeq) {
        RegionDetailResDto resDto = regionService.getRegionDetail(regionSeq);

        return ResponseEntity.ok(ResponseDto.<RegionDetailResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }


    @PutMapping("/{regionSeq}")
    @Operation(summary = "권역 수정", description = "권역정보를 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyRegion(
            @PathVariable Long regionSeq
            , @RequestBody @Validated ModifyRegionReqDto reqDto
    ) {
        regionService.modifyRegion(regionSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    @DeleteMapping("/{regionSeq}")
    @Operation(summary = "권역 삭제", description = "권역정보를 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> removeRegion(@PathVariable Long regionSeq) {
        regionService.removeRegion(regionSeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }

}
