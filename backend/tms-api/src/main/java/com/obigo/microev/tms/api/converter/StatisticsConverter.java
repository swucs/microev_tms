package com.obigo.microev.tms.api.converter;

import com.obigo.microev.tms.api.presentation.statistics.GetMonthlyResDto;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatisticsByDayResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatisticsSummaryResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatisticsConverter {
    List<GetMonthlyResDto.DailyStatistics> toGetMonthlyResDtoDailyStatistics(List<DeliveryStatisticsByDayResult> dailyStatistics);

    GetMonthlyResDto.Summary toGetMonthlyResDtoSummary(DeliveryStatisticsSummaryResult statisticsSummary);
}
