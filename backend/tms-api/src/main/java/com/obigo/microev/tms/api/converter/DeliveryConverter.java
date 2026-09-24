package com.obigo.microev.tms.api.converter;


import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUserContextHolder;
import com.obigo.microev.tms.api.presentation.delivery.GetDeliveryStatusResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetMainResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetWaybillsResDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessCompletedReqDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessUncompletedReqDto;
import com.obigo.microev.tms.core.domain.entity.Delivery;
import com.obigo.microev.tms.core.domain.entity.DeliveryHistory;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryCountByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatusByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.WaybillResult;
import com.obigo.microev.tms.lib.vo.MqttDeliveryMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, CurrentUserContextHolder.class}
)
public interface DeliveryConverter {
    GetMainResDto toGetMainResDto(DeliveryCountByDriverSeqResult deliveryCount);

    GetDeliveryStatusResDto toGetDeliveryStatusResDto(DeliveryCountByDriverSeqResult deliveryCount);

    List<GetDeliveryStatusResDto.DeliverySummary> toDeliverySummaries(List<DeliveryStatusByDriverSeqResult> summaries);

    List<GetWaybillsResDto.Waybill> toWaybills(List<WaybillResult> waybillResults);

    GetWaybillsResDto toGetWaybillsResDto(DeliveryStatusByDriverSeqResult deliveryStatusResult);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(CurrentUserContextHolder.get().getDriverSeq())")
    DeliveryHistory toDeliveryHistory(Delivery delivery);

    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(CurrentUserContextHolder.get().getDriverSeq())")
    void updateDelivery(@MappingTarget Delivery delivery, ProcessCompletedReqDto processCompletedReqDto);

    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(CurrentUserContextHolder.get().getDriverSeq())")
    void updateDelivery(@MappingTarget Delivery delivery, ProcessUncompletedReqDto processUncompletedReqDto);

    @Mapping(target = "eventDatetime", expression = "java(LocalDateTime.now())")
    MqttDeliveryMessage toMqttDeliveryMessage(Delivery delivery);
}
