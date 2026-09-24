package com.obigo.microev.tms.admin.presentation.dispatch;

import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.DispatchService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "배차계획")
@RequiredArgsConstructor
@RestController
@RequestMapping("/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;


    @GetMapping
    @Operation(summary = "배차관리(배차현황) 목록 조회", description = "배차관리(배차현황) 목록을 검색하고 조회한다.")
    public ResponseEntity<ResponseDto<List<SearchDispatchesResDto>>> searchDispatches(
            @ModelAttribute @Validated @ParameterObject SearchDispatchesReqDto reqDto
    ) {

        List<SearchDispatchesResDto> resDtos = dispatchService.searchDispatches(reqDto);

        return ResponseEntity.ok(ResponseDto.<List<SearchDispatchesResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDtos)
                .build());
    }


    @PostMapping
    @Operation(summary = "배차생성", description = "배차기본 정보를 생성한다.")
    public ResponseEntity<ResponseDto<Long>> createDispatch(@RequestBody @Validated CreateDispatchReqDto reqDto) {

        long dispatchSeq = dispatchService.createDispatch(reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(dispatchSeq)
                .build());
    }

    @PutMapping("/{dispatchSeq}")
    @Operation(summary = "배차수정", description = "배차기본 정보를 수정한다.")
    public ResponseEntity<ResponseDto<Long>> modifyDispatch(
            @PathVariable("dispatchSeq") Long dispatchSeq,
            @RequestBody @Validated ModifyDispatchReqDto reqDto
    ) {
        dispatchService.modifyDispatch(dispatchSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(null)
                .build());
    }


    @GetMapping("/{dispatchSeq}")
    @Operation(summary = "배차상세", description = "배차상세 정보를 조회한다.")
    public ResponseEntity<ResponseDto<DispatchDetailResDto>> getDispatch(@PathVariable("dispatchSeq") Long dispatchSeq) {

        DispatchDetailResDto resDto = dispatchService.getDispatchDetail(dispatchSeq);

        return ResponseEntity.ok(ResponseDto.<DispatchDetailResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

    @PutMapping("/{dispatchSeq}/dispatched")
    @Operation(summary = "배차확정-배송정보의 순서도 저장", description = "배차를 확정하면서 배송정보의 순서도 저장한다.")
    public ResponseEntity<ResponseDto<Void>> modifyStatusToDispatched(
            @PathVariable("dispatchSeq") Long dispatchSeq,
            @RequestBody @Validated ModifyStatusToDispatchedReqDto reqDto
    ) {

        dispatchService.modifyStatusToDispatched(dispatchSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(null)
                .build());
    }

    @PutMapping("/{dispatchSeq}/waiting")
    @Operation(summary = "배차대기 상태로 변경", description = "배차대기 상태로 변경한다. 배송출발/배송완료 한 데이터가 없는 경우만 가능하다.")
    public ResponseEntity<ResponseDto<Void>> modifyStatusToWaiting(@PathVariable("dispatchSeq") Long dispatchSeq) {

        dispatchService.modifyStatusToWaiting(dispatchSeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(null)
                .build());
    }

    @DeleteMapping("/{dispatchSeq}")
    @Operation(summary = "배차정보 삭제", description = "배차정보를 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> removeDispatch(@PathVariable("dispatchSeq") Long dispatchSeq) {

        dispatchService.removeDispatch(dispatchSeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(null)
                .build());
    }


    @PostMapping("/autoDispatch")
    @Operation(summary = "자동 차량배정", description = "배송정보 화면에서 주소 입력시 권역 변환 및 권역에 따른 차량배정 정보를 반환한다.")
    public ResponseEntity<ResponseDto<AutoDispatchResDto>> autoDispatch(@RequestBody @Validated AutoDispatchReqDto reqDto) {
        AutoDispatchResDto resDto = dispatchService.autoDispatch(reqDto);

        return ResponseEntity.ok(ResponseDto.<AutoDispatchResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

    
    
    @PostMapping("/delivery")
    @Operation(summary = "배송정보 추가", description = "배차의 배송정보를 추가한다.")
    public ResponseEntity<ResponseDto<Long>> createDelivery(@RequestBody @Validated CreateDeliveryReqDto reqDto) {

        long deliverySeq = dispatchService.createDelivery(reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(deliverySeq)
                .build());
    }


    @PutMapping("/delivery/{deliverySeq}")
    @Operation(summary = "배송정보 수정", description = "배송정보를 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyDelivery(
            @PathVariable("deliverySeq") Long deliverySeq,
            @RequestBody @Validated ModifyDeliveryReqDto reqDto
    ) {

        dispatchService.modifyDelivery(deliverySeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(null)
                .build());
    }


    @DeleteMapping("/delivery/{deliverySeq}")
    @Operation(summary = "배송정보 삭제", description = "배송정보를 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> removeDelivery(@PathVariable("deliverySeq") Long deliverySeq) {

        dispatchService.removeDelivery(deliverySeq);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(null)
                .build());
    }


    @GetMapping("/delivery/excel/download-sample-delivery/{dispatchSeq}")
    @Operation(summary = "배차의 배송정보 샘플 엑셀 다운로드", description = "배차의 배송정보 샘플 엑셀파일을 다운로드한다.")
    public void downloadSampleDelivery(@PathVariable("dispatchSeq") Long dispatchSeq, HttpServletResponse response) throws Exception {

        CreateExcelFileForDeliveryResDto resDto = dispatchService.createExcelFileForSampleDelivery(dispatchSeq);

        String fileName = resDto.getFileName();
        String encodedFileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFileName);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", contentDisposition);

        Workbook workbook = resDto.getWorkbook();
        workbook.write(response.getOutputStream());
        workbook.close();

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


    @PostMapping("/delivery/excel/upload/{dispatchSeq}")
    @Operation(summary = "배송정보 엑셀 업로드", description = "배차의 배송정보를 엑셀파일로 업로드 하여, 데이터를 저장한다.")
    public ResponseEntity<ResponseDto<Void>> uploadDeliveryExcel(@PathVariable("dispatchSeq") Long dispatchSeq
            , @RequestParam("file") MultipartFile file) {

        dispatchService.readAndSaveDeliveryExcel(dispatchSeq, file);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    @GetMapping("/delivery/excel/download/{dispatchSeq}")
    @Operation(summary = "배차의 실제 배송정보 엑셀 다운로드", description = "배차의 실제 배송정보를 엑셀파일로 다운로드한다.")
    public void downloadDeliveryExcel(@PathVariable("dispatchSeq") Long dispatchSeq, HttpServletResponse response) throws Exception {
        CreateExcelFileForDeliveryResDto resDto = dispatchService.createExcelFileForDelivery(dispatchSeq);

        String fileName = resDto.getFileName();
        String encodedFileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFileName);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", contentDisposition);

        Workbook workbook = resDto.getWorkbook();
        workbook.write(response.getOutputStream());
        workbook.close();

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
