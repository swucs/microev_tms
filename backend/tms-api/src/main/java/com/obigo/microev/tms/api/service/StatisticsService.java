package com.obigo.microev.tms.api.service;

import com.obigo.microev.tms.api.presentation.statistics.GetMonthlyReqDto;
import com.obigo.microev.tms.api.presentation.statistics.GetMonthlyResDto;

public interface StatisticsService {
    GetMonthlyResDto getMonthlyStatistics(GetMonthlyReqDto reqDto);
}
