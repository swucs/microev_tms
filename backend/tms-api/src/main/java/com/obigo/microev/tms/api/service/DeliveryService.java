package com.obigo.microev.tms.api.service;

import com.obigo.microev.tms.api.presentation.delivery.GetDeliveryStatusResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetMainResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetWaybillsReqDto;
import com.obigo.microev.tms.api.presentation.delivery.GetWaybillsResDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessCompletedReqDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessCompletedResDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessUncompletedReqDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessUncompletedResDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface DeliveryService {
    GetMainResDto getMainInfo();

    GetDeliveryStatusResDto getDeliveryStatus();

    GetWaybillsResDto getWaybills(GetWaybillsReqDto reqDto);

    ProcessCompletedResDto processCompleted(Long deliverySeq, ProcessCompletedReqDto reqDto, MultipartFile attachFile);

    ProcessUncompletedResDto processUncompleted(Long deliverySeq, ProcessUncompletedReqDto reqDto);

    SseEmitter subscribe();
}
