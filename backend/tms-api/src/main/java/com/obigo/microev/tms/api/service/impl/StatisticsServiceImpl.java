package com.obigo.microev.tms.api.service.impl;

import com.obigo.microev.tms.api.converter.StatisticsConverter;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUser;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUserContextHolder;
import com.obigo.microev.tms.api.presentation.statistics.GetMonthlyReqDto;
import com.obigo.microev.tms.api.presentation.statistics.GetMonthlyResDto;
import com.obigo.microev.tms.api.service.StatisticsService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.DeliveryMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatisticsByDayResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatisticsSummaryResult;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    private final DeliveryMapper deliveryMapper;
    private final StatisticsConverter statisticsConverter;

    /**
     * 해당 배송기사의 월별 배송 통계
     * @param reqDto
     * @return
     */
    @Override
    public GetMonthlyResDto getMonthlyStatistics(GetMonthlyReqDto reqDto) {
        CurrentUser currentUser = CurrentUserContextHolder.get();
        Long driverSeq = currentUser.getDriverSeq();

        String yyyyMM = reqDto.getYyyyMM();


        LocalDate deliveryStartDate = null;
        LocalDate deliveryEndDate = null;
        try {
            deliveryStartDate = LocalDate.parse(yyyyMM + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
            deliveryEndDate = deliveryStartDate.plusMonths(1).minusDays(1);
        } catch (Exception e) {
            throw new InvalidRequestException(ResponseCode.INVALID_STATISTICS_SEARCH_YYYYMM);
        }

        GetMonthlyResDto resDto = new GetMonthlyResDto();

        //월별 통계 요약
        DeliveryStatisticsSummaryResult statisticsSummary = deliveryMapper.findStatisticsSummary(driverSeq, deliveryStartDate, deliveryEndDate);
        GetMonthlyResDto.Summary summary = statisticsConverter.toGetMonthlyResDtoSummary(statisticsSummary);
        resDto.setSummary(summary);


        //일별 통계 목록
        List<DeliveryStatisticsByDayResult> dailyStatistics = deliveryMapper.findDailyStatistics(driverSeq, deliveryStartDate, deliveryEndDate);
        List<GetMonthlyResDto.DailyStatistics> dailies = statisticsConverter.toGetMonthlyResDtoDailyStatistics(dailyStatistics);
        resDto.setDailies(dailies);

        return resDto;
    }


}
