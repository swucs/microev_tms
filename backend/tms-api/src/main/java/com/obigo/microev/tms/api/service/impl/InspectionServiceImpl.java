package com.obigo.microev.tms.api.service.impl;

import com.obigo.microev.tms.api.converter.DeliveryConverter;
import com.obigo.microev.tms.api.converter.InspectionConverter;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUser;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUserContextHolder;
import com.obigo.microev.tms.api.presentation.inspection.GetMobileInspectionResDto;
import com.obigo.microev.tms.api.service.InspectionService;
import com.obigo.microev.tms.core.domain.entity.Delivery;
import com.obigo.microev.tms.core.domain.entity.DeliveryHistory;
import com.obigo.microev.tms.core.domain.entity.Dispatch;
import com.obigo.microev.tms.core.domain.enumeration.DeliveryStatus;
import com.obigo.microev.tms.core.domain.enumeration.DispatchStatus;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.DeliveryHistoryMapper;
import com.obigo.microev.tms.core.domain.mapper.DeliveryMapper;
import com.obigo.microev.tms.core.domain.mapper.DispatchMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.WaybillResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.DispatchDetailResult;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import com.obigo.microev.tms.lib.publisher.MqttPublisher;
import com.obigo.microev.tms.lib.vo.MqttDeliveryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InspectionServiceImpl implements InspectionService {

    private final DispatchMapper dispatchMapper;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryHistoryMapper deliveryHistoryMapper;
    private final InspectionConverter inspectionConverter;
    private final DeliveryConverter deliveryConverter;
    private final MqttPublisher mqttPublisher;



    /**
     * 운송장 정보 조회
     * @return
     */
    @Override
    public GetMobileInspectionResDto getInspections() {

        CurrentUser currentUser = CurrentUserContextHolder.get();
        Long driverSeq = currentUser.getDriverSeq();

        //해당 기사의 오늘의 배차 정보 조회
        LocalDate today = LocalDate.now();
        DispatchDetailResult dispatchDetailResult = dispatchMapper.findByDriverSeq(driverSeq, today);
        if (dispatchDetailResult == null) {
            return new GetMobileInspectionResDto();
        }

        Long dispatchSeq = dispatchDetailResult.getDispatchSeq();

        //검수 목록 조회
        List<WaybillResult> waybillResults = deliveryMapper.findWaybills(driverSeq, dispatchSeq, null);
        List<GetMobileInspectionResDto.Inspection> inspections = inspectionConverter.GetMobileInspectionResDtoInspection(waybillResults);

        GetMobileInspectionResDto resDto = new GetMobileInspectionResDto();
        inspections
                .forEach(inspection -> {

                    Integer boxCount = inspection.getBoxCount();
                    if (DeliveryStatus.INSPECTION_WAITING.name().equals(inspection.getDeliveryStatusCd())) {
                        resDto.setInspectionWaitingCount(resDto.getInspectionWaitingCount() + boxCount);
                    } else {
                        resDto.setInspectionCompletedCount(resDto.getInspectionCompletedCount() + boxCount);
                    }
                    resDto.setTotalBoxCount(resDto.getTotalBoxCount() + boxCount);

                });
        resDto.setInspections(inspections);
        return resDto;
    }


    /**
     * 검수완료 처리
     * @param deliverySeq
     */
    @Override
    public void processInspectionCompleted(Long deliverySeq) {
        Delivery delivery = deliveryMapper.findById(deliverySeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DELIVERY));

        //검수대기 상태가 아니면 검수완료 처리 불가
        if (!DeliveryStatus.INSPECTION_WAITING.name().equals(delivery.getDeliveryStatusCd())) {
            throw new InvalidRequestException(ResponseCode.IMPOSSIBLE_INSPECTION_NOT_INSPECTION_WAITING);
        }

        LocalDateTime now = LocalDateTime.now();
        Long driverSeq = CurrentUserContextHolder.get().getDriverSeq();

        //검수완료 상태로 Update
        delivery.setDeliveryStatusCd(DeliveryStatus.INSPECTION_COMPLETED.name());
        delivery.setModifiedAt(now);
        delivery.setModifierSeq(driverSeq);
        deliveryMapper.update(delivery);

        //배송이력 추가
        this.createDevlieryHistory(delivery);

        //MQTT 이벤트 전송
        this.sendMqttDeliveryEvent(delivery);


        //해당 배차상태가 배송중이 아니라면 배송중으로 변경
        Dispatch dispatch = dispatchMapper.findById(delivery.getDispatchSeq())
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DISPATCH));

        if (!DispatchStatus.IN_DELIVERY.name().equals(dispatch.getDispatchStatusCd())) {
            dispatch.setDispatchStatusCd(DispatchStatus.IN_DELIVERY.name());
            dispatch.setModifiedAt(now);
            dispatch.setModifierSeq(driverSeq);
            dispatchMapper.update(dispatch);
        }
    }


    /**
     * 검수완료 취소 처리
     * @param deliverySeq
     */
    @Override
    public void processInspectionCancel(Long deliverySeq) {
        LocalDateTime now = LocalDateTime.now();
        Long driverSeq = CurrentUserContextHolder.get().getDriverSeq();


        Delivery delivery = deliveryMapper.findById(deliverySeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DELIVERY));

        //검수완료 상태가 아니면 검수취소 처리 불가
        if (!DeliveryStatus.INSPECTION_COMPLETED.name().equals(delivery.getDeliveryStatusCd())) {
            throw new InvalidRequestException(ResponseCode.IMPOSSIBLE_INSPECTION_CANCEL_NOT_INSPECTION_COMPLETED);
        }

        //해당 배송기사가 이미 배송시작한 경우라면 취소 불가
        String driverDeliveryStatus = deliveryMapper.findDriverDeliveryStatus(driverSeq, delivery.getDispatchSeq());
        if (!StringUtils.containsAny(driverDeliveryStatus
                , new String[] {DeliveryStatus.INSPECTION_WAITING.name(), DeliveryStatus.INSPECTION_COMPLETED.name()})) {
            throw new InvalidRequestException(ResponseCode.IMPOSSIBLE_INSPECTION_CANCEL_IN_DELIVERY);
        }

        //검수대기 상태로 Update
        delivery.setDeliveryStatusCd(DeliveryStatus.INSPECTION_WAITING.name());
        delivery.setModifiedAt(now);
        delivery.setModifierSeq(driverSeq);
        deliveryMapper.update(delivery);

        //배송이력 추가
        this.createDevlieryHistory(delivery);

        //MQTT 이벤트 전송
        this.sendMqttDeliveryEvent(delivery);

        //해당 배차상태가 검수완료라면 배차상태를 배차확정으로 변경
        Dispatch dispatch = dispatchMapper.findById(delivery.getDispatchSeq())
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DISPATCH));

        if (DispatchStatus.IN_DELIVERY.name().equals(dispatch.getDispatchStatusCd())) {
            dispatch.setDispatchStatusCd(DispatchStatus.DISPATCHED.name());
            dispatch.setModifiedAt(now);
            dispatch.setModifierSeq(driverSeq);
            dispatchMapper.update(dispatch);
        }
    }

    /**
     * 배송이력 생성
     * @param delivery
     */
    private void createDevlieryHistory(Delivery delivery) {
        DeliveryHistory deliveryHistory = deliveryConverter.toDeliveryHistory(delivery);
        deliveryHistoryMapper.insert(deliveryHistory);
    }

    /**
     * 배송정보가 변경되었음을 알리는 MQTT 이벤트 전송
     * @param delivery
     */
    private void sendMqttDeliveryEvent(Delivery delivery) {
        MqttDeliveryMessage mqttDeliveryMessage = deliveryConverter.toMqttDeliveryMessage(delivery);
        mqttPublisher.sendChangedDelivery(delivery.getDriverSeq(), mqttDeliveryMessage);
    }

}
