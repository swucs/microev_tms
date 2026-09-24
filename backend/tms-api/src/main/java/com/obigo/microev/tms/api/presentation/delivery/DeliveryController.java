package com.obigo.microev.tms.api.presentation.delivery;

import com.obigo.microev.tms.api.presentation.ResponseDto;
import com.obigo.microev.tms.api.service.DeliveryService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "배송관련 API")
@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/main")
    @Operation(summary = "메인화면 정보", description = "메인화면에 필요한 정보를 반환한다.")
    public ResponseEntity<ResponseDto<GetMainResDto>> getMain() {
        GetMainResDto resDto = deliveryService.getMainInfo();

        return ResponseEntity.ok(ResponseDto.<GetMainResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }


    @GetMapping("/status")
    @Operation(summary = "배송현황", description = "배송현황 정보를 반환한다.")
    public ResponseEntity<ResponseDto<GetDeliveryStatusResDto>> status() {
        GetDeliveryStatusResDto resDto = deliveryService.getDeliveryStatus();

        return ResponseEntity.ok(ResponseDto.<GetDeliveryStatusResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

    @GetMapping("/waybill")
    @Operation(summary = "운송장 리스트 조회", description = "운송장 리스트를 반환한다.")
    public ResponseEntity<ResponseDto<GetWaybillsResDto>> waybills(
            @ModelAttribute @Validated @ParameterObject GetWaybillsReqDto reqDto
    ) {
        GetWaybillsResDto resDto = deliveryService.getWaybills(reqDto);

        return ResponseEntity.ok(ResponseDto.<GetWaybillsResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }


    @PutMapping("/mobile/{deliverySeq}/completed")
    @Operation(summary = "배송완료 처리", description = "해당 배송정보에 대해 배송완료 처리한다.")
    public ResponseEntity<ResponseDto<ProcessCompletedResDto>> processCompleted(
            @PathVariable Long deliverySeq,
            @RequestPart(value = "deliveryCompleted") @Validated ProcessCompletedReqDto reqDto,
            @RequestPart(value = "attachFile", required = false) MultipartFile attachFile
    ) {
        ProcessCompletedResDto resDto = deliveryService.processCompleted(deliverySeq, reqDto, attachFile);

        return ResponseEntity.ok(ResponseDto.<ProcessCompletedResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }


    @PutMapping("/mobile/{deliverySeq}/uncompleted")
    @Operation(summary = "미배송/미수거 처리", description = "해당 배송정보에 대해 미배송/미수거 처리한다.")
    public ResponseEntity<ResponseDto<ProcessUncompletedResDto>> processUncompleted(
            @PathVariable Long deliverySeq,
            @RequestBody @Validated ProcessUncompletedReqDto reqDto
    ) {
        ProcessUncompletedResDto resDto = deliveryService.processUncompleted(deliverySeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<ProcessUncompletedResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return deliveryService.subscribe();
    }
}
