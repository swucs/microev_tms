package com.obigo.microev.tms.admin.presentation.commonCode;

import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.service.CommonCodeService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "공통코드 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/common-code")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    @GetMapping(value = "/valid-common-codes/{comCodeGroupCd}")
    @Operation(summary = "유효 공통코드 목록 조회", description = "유효한 공통코드 조회한다.")
    public ResponseEntity<ResponseDto<List<GetValidCommonCodesResDto>>> getValidCommonCodes(
            @PathVariable("comCodeGroupCd") String comCodeGroupCd
    ) {

        List<GetValidCommonCodesResDto> validCommonCodes = commonCodeService.getValidCommonCodes(comCodeGroupCd);

        return ResponseEntity.ok(ResponseDto.<List<GetValidCommonCodesResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(validCommonCodes)
                .build());
    }

    /**
     * 그룹코드 목록 조회
     *
     * @param reqDto
     * @return
     */
    @GetMapping(value = "/common-code-groups")
    @Operation(summary = "그룹코드 목록 조회", description = "검색조건으로 그룹코드 목록 조회한다.")
    public ResponseEntity<ResponseDto<List<SearchCommonCodeGroupsResDto>>> searchCommonCodeGroups(
            @ModelAttribute @Validated SearchCommonCodeGroupsReqDto reqDto
    ) {

        List<SearchCommonCodeGroupsResDto> groupCodes = commonCodeService.searchCommonCodeGroups(reqDto);

        return ResponseEntity.ok(ResponseDto.<List<SearchCommonCodeGroupsResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(groupCodes)
                .build());
    }

    /**
     * 그룹코드 생성
     **/
    @PostMapping(value = "/common-code-group")
    @Operation(summary = "그룹코드 생성", description = "새로운 그룹코드 생성한다.")
    public ResponseEntity<ResponseDto<CreateCommonCodeGroupResDto>> createCommonCodeGroup(
            @RequestBody @Validated CreateCommonCodeGroupReqDto reqDto
    ) {

        CreateCommonCodeGroupResDto resDto = commonCodeService.createCommonCodeGroup(reqDto);

        return ResponseEntity.ok(ResponseDto.<CreateCommonCodeGroupResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

    /**
     * 그룹코드 수정
     **/
    @PutMapping(value = "/common-code-group")
    @Operation(summary = "그룹코드 수정", description = "그룹코드 수정한다.")
    public ResponseEntity<ResponseDto<ModifyCommonCodeGroupResDto>> modifyCommonCodeGroup(
            @RequestBody @Validated ModifyCommonCodeGroupReqDto reqDto
    ) {

        ModifyCommonCodeGroupResDto resDto = commonCodeService.modifyCommonCodeGroup(reqDto);

        return ResponseEntity.ok(ResponseDto.<ModifyCommonCodeGroupResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());
    }

    /**
     * 그룹코드 삭제
     **/
    @DeleteMapping(value = "/common-code-group")
    @Operation(summary = "그룹코드 삭제", description = "그룹코드 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> deleteCommonCodeGroup(
            @RequestBody @Validated DeleteCommonCodeGroupReqDto reqDto
    ) {

        commonCodeService.deleteCommonCodeGroups(reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    /**
     * 공통코드 전체 목록 조회 by 그룹코드
     *
     * @param comCodeGroupCd
     * @return
     */
    @GetMapping(value = "/common-codes/{comCodeGroupCd}")
    @Operation(summary = "공통코드 목록 조회", description = "검색조건으로 공통코드 목록 조회한다.")
    public ResponseEntity<ResponseDto<List<GetCommonCodesResDto>>> getCommonCodes(
            @PathVariable("comCodeGroupCd") String comCodeGroupCd
    ) {

        List<GetCommonCodesResDto> commonCodes = commonCodeService.getCommonCodes(comCodeGroupCd);

        return ResponseEntity.ok(ResponseDto.<List<GetCommonCodesResDto>>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(commonCodes)
                .build());
    }

    /**
     * 공통코드 생성
     **/
    @PostMapping(value = "/common-code")
    @Operation(summary = "공통코드 생성", description = "새로운 공통코드 생성한다.")
    public ResponseEntity<ResponseDto<Long>> createCommonCode(
            @RequestBody @Validated CreateCommonCodeReqDto reqDto
    ) {

        Long comCodeSeq = commonCodeService.createCommonCodes(reqDto);

        return ResponseEntity.ok(ResponseDto.<Long>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(comCodeSeq)
                .build());
    }



    /**
     * 공통코드 수정
     **/
    @PutMapping(value = "/common-code/{comCodeSeq}")
    @Operation(summary = "공통코드 수정", description = "새로운 공통코드 수정한다.")
    public ResponseEntity<ResponseDto<Void>> modifyCommonCode(
            @PathVariable("comCodeSeq") Long comCodeSeq,
            @RequestBody @Validated ModifyCommonCodeReqDto reqDto
    ) {
        commonCodeService.modifyCommonCodes(comCodeSeq, reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }


    @DeleteMapping(value = "/common-code")
    @Operation(summary = "공통코드 삭제", description = "공통코드 삭제한다.")
    public ResponseEntity<ResponseDto<Void>> deleteCommonCode(
            @RequestBody @Validated DeleteCommonCodeReqDto reqDto
    ) {

        commonCodeService.deleteCommonCodes(reqDto);

        return ResponseEntity.ok(ResponseDto.<Void>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .build());
    }

}
