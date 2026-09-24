package com.obigo.microev.tms.api.presentation.statistics;


import com.obigo.microev.tms.api.presentation.ResponseDto;
import com.obigo.microev.tms.api.service.StatisticsService;
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

@Tag(name = "통계관련 API")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/monthly")
    @Operation(summary = "해당 배송기사의 월별 배송 통계", description = "해당 배송기사의 월별 배송 통계를 반환한다.")
    public ResponseEntity<ResponseDto<GetMonthlyResDto>> monthly(
            @ModelAttribute @Validated @ParameterObject GetMonthlyReqDto reqDto
    ) {
        GetMonthlyResDto resDto = statisticsService.getMonthlyStatistics(reqDto);

        return ResponseEntity.ok(ResponseDto.<GetMonthlyResDto>builder()
                .resultCode(ResponseCode.SUCCESS.getCode())
                .resultMessage(ResponseCode.SUCCESS.getMessage())
                .data(resDto)
                .build());

    }
}
