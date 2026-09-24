package com.obigo.microev.tms.api.presentation.inspection;

import com.obigo.microev.tms.api.presentation.ResponseDto;
import com.obigo.microev.tms.api.service.InspectionService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품검수관련 API")
@RestController
@RequestMapping("/inspection")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService inspectionService;

    @GetMapping("/mobile")
    @Operation(summary = "상품검수 리스트 조회", description = "상품검수 리스트를 반환한다.")
    public ResponseEntity<ResponseDto<GetMobileInspectionResDto>> getMobileInspection() {
        GetMobileInspectionResDto resDto = inspectionService.getInspections();

        return ResponseEntity.ok(ResponseDto.<GetMobileInspectionResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());

    }

    @PutMapping("/mobile/{deliverySeq}/completed")
    @Operation(summary = "상품의 검수완료 처리", description = "상품의 검수완료 처리")
    public ResponseEntity<ResponseDto<Void>> processCompleted(@PathVariable("deliverySeq") Long deliverySeq) {
        inspectionService.processInspectionCompleted(deliverySeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    @PutMapping("/mobile/{deliverySeq}/cancel")
    @Operation(summary = "상품의 검수취소 처리", description = "상품의 검수취소 처리")
    public ResponseEntity<ResponseDto<Void>> processCancel(@PathVariable("deliverySeq") Long deliverySeq) {
        inspectionService.processInspectionCancel(deliverySeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }
}
