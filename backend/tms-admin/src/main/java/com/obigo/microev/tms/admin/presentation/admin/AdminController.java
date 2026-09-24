package com.obigo.microev.tms.admin.presentation.admin;

import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.AdminService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    @Operation(summary = "관리자 목록 조회", description = "관리자 목록을 검색하고 조회한다.")
    public ResponseEntity<ResponseDto<List<SearchAdminsResDto>>> searchAdmins(
            @ModelAttribute @Validated @ParameterObject SearchAdminsReqDto reqDto
    ) {

        List<SearchAdminsResDto> data = adminService.searchAdmins(reqDto);
        return ResponseEntity.ok(ResponseDto.<List<SearchAdminsResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build());
    }


    @PostMapping
    @Operation(summary = "관리자 등록", description = "관리자를 등록한다.")
    public ResponseEntity<ResponseDto<Long>> createAdmin(@RequestBody @Validated CreateAdminReqDto reqDto) {

        long adminSeq = adminService.createAdmin(reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(adminSeq)
                .build());
    }


    @PutMapping("/{adminSeq}")
    @Operation(summary = "관리자 수정", description = "관리자를 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyAdmin(
            @PathVariable Long adminSeq
            , @RequestBody @Validated ModifyAdminReqDto reqDto
    ) {
        adminService.modifyAdmin(adminSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }

}
