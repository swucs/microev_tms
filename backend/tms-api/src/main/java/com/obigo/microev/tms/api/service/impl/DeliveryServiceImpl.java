package com.obigo.microev.tms.api.service.impl;

import com.obigo.microev.tms.api.converter.DeliveryConverter;
import com.obigo.microev.tms.api.infrastructure.sse.SseHandler;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUser;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUserContextHolder;
import com.obigo.microev.tms.api.presentation.delivery.GetDeliveryStatusResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetMainResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetWaybillsReqDto;
import com.obigo.microev.tms.api.presentation.delivery.GetWaybillsResDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessCompletedReqDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessCompletedResDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessUncompletedReqDto;
import com.obigo.microev.tms.api.presentation.delivery.ProcessUncompletedResDto;
import com.obigo.microev.tms.api.service.DeliveryService;
import com.obigo.microev.tms.core.domain.entity.Delivery;
import com.obigo.microev.tms.core.domain.entity.DeliveryHistory;
import com.obigo.microev.tms.core.domain.enumeration.AttachSubDirectory;
import com.obigo.microev.tms.core.domain.enumeration.DeliveryStatus;
import com.obigo.microev.tms.core.domain.enumeration.DispatchStatus;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.DeliveryHistoryMapper;
import com.obigo.microev.tms.core.domain.mapper.DeliveryMapper;
import com.obigo.microev.tms.core.domain.mapper.DispatchMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryCountByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatusByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.WaybillResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.DispatchDetailResult;
import com.obigo.microev.tms.core.domain.service.UploadService;
import com.obigo.microev.tms.core.exception.BusinessException;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import com.obigo.microev.tms.api.infrastructure.sse.DeliveryChangedEvent;
import com.obigo.microev.tms.api.infrastructure.sse.DeliveryEventNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DispatchMapper dispatchMapper;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryHistoryMapper deliveryHistoryMapper;
    private final DeliveryConverter deliveryConverter;
    private final UploadService uploadService;
    private final DeliveryEventNotifier deliveryEventNotifier;
    private final SseHandler sseHandler;

    /**
     * 메인화면 정보 조회
     * @return
     */
    @Override
    public GetMainResDto getMainInfo() {
        CurrentUser currentUser = CurrentUserContextHolder.get();
        Long driverSeq = currentUser.getDriverSeq();

        //해당 기사의 오늘의 배차 정보 조회
        LocalDate today = LocalDate.now();
        DispatchDetailResult dispatchDetailResult = dispatchMapper.findByDriverSeq(driverSeq, today);
        if (dispatchDetailResult == null) {
            return new GetMainResDto();
        }

        DeliveryCountByDriverSeqResult deliveryCount = deliveryMapper.findDeliveryCountByDriverSeq(driverSeq, dispatchDetailResult.getDispatchSeq());
        GetMainResDto resDto = deliveryConverter.toGetMainResDto(deliveryCount);

        //해당 배차의 배송기사 배송상태
        String driverDeliveryStatus = deliveryMapper.findDriverDeliveryStatus(driverSeq, dispatchDetailResult.getDispatchSeq());
        resDto.setDriverDeliveryStatus(driverDeliveryStatus);
        return resDto;
    }


    /**
     * 배송현황 정보 조회
     * @return
     */
    @Override
    public GetDeliveryStatusResDto getDeliveryStatus() {
        CurrentUser currentUser = CurrentUserContextHolder.get();
        Long driverSeq = currentUser.getDriverSeq();
        LocalDateTime now = LocalDateTime.now();

        //해당 기사의 오늘의 배차 정보 조회
        LocalDate today = LocalDate.now();
        DispatchDetailResult dispatchDetailResult = dispatchMapper.findByDriverSeq(driverSeq, today);
        if (dispatchDetailResult == null) {
            return new GetDeliveryStatusResDto();
        }

        //해당 배송기사의 배송정보에 검수대기 상태가 있는지 확인
        List<Delivery> deliveries = deliveryMapper.findByDispatchSeqAndDriverSeq(dispatchDetailResult.getDispatchSeq(), driverSeq);
        boolean existsInspectionWaiting = deliveries
                .stream()
                .anyMatch(delivery -> DeliveryStatus.INSPECTION_WAITING.name().equals(delivery.getDeliveryStatusCd()));

        if (existsInspectionWaiting) {
            throw new InvalidRequestException(ResponseCode.EXISTS_INSPECTION_WAITING);
        }

        //해당 배송기사가 배송출발을 했으므로 배송정보의 상태를 배송중으로 변경
        deliveries.stream()
                .filter(delivery -> DeliveryStatus.INSPECTION_COMPLETED.name().equals(delivery.getDeliveryStatusCd()))
                .forEach(delivery -> {
                    delivery.setDeliveryStatusCd(DeliveryStatus.IN_DELIVERY.name());
                    delivery.setModifiedAt(now);
                    delivery.setModifierSeq(driverSeq);
                    deliveryMapper.update(delivery);

                    //이력생성
                    this.createDevlieryHistory(delivery);

                    //배송상태 변경 이벤트 전송(SSE)
                    this.sendDeliveryEvent(delivery);
                });


        //배송처리 카운트 조회
        DeliveryCountByDriverSeqResult deliveryCount = deliveryMapper.findDeliveryCountByDriverSeq(driverSeq, dispatchDetailResult.getDispatchSeq());

        //주소1별 배송현황 목록 조회
        List<DeliveryStatusByDriverSeqResult> summaries = deliveryMapper.findDeliveryStatusByDriverSeq(driverSeq, dispatchDetailResult.getDispatchSeq(), null);
        List<GetDeliveryStatusResDto.DeliverySummary> deliverySummaries = deliveryConverter.toDeliverySummaries(summaries);

        GetDeliveryStatusResDto resDto = deliveryConverter.toGetDeliveryStatusResDto(deliveryCount);
        resDto.setDeliverySummaries(deliverySummaries);

        //해당 배차의 배송기사 배송상태
        String driverDeliveryStatus = deliveryMapper.findDriverDeliveryStatus(driverSeq, dispatchDetailResult.getDispatchSeq());
        resDto.setDriverDeliveryStatus(driverDeliveryStatus);

        return resDto;
    }


    /**
     * 운송장 정보 조회
     * @return
     */
    @Override
    public GetWaybillsResDto getWaybills(GetWaybillsReqDto reqDto) {

        CurrentUser currentUser = CurrentUserContextHolder.get();
        Long driverSeq = currentUser.getDriverSeq();
        String address = reqDto.getAddress();

        //해당 기사의 오늘의 배차 정보 조회
        LocalDate today = LocalDate.now();
        DispatchDetailResult dispatchDetailResult = dispatchMapper.findByDriverSeq(driverSeq, today);
        if (dispatchDetailResult == null) {
            return new GetWaybillsResDto();
        }

        //배송처리 카운트 조회
        Long dispatchSeq = dispatchDetailResult.getDispatchSeq();
        List<DeliveryStatusByDriverSeqResult> deliveryStatusResults = deliveryMapper.findDeliveryStatusByDriverSeq(driverSeq, dispatchSeq, address);
        if (deliveryStatusResults.isEmpty()) {
            return new GetWaybillsResDto();
        }
        GetWaybillsResDto resDto = deliveryConverter.toGetWaybillsResDto(deliveryStatusResults.getFirst());
        resDto.setAddress(address);

        //운송장 정보 조회
        List<WaybillResult> waybillResults = deliveryMapper.findWaybills(driverSeq, dispatchSeq, address);
        List<GetWaybillsResDto.Waybill> waybills = deliveryConverter.toWaybills(waybillResults);
        resDto.setWaybills(waybills);

        return resDto;
    }


    /**
     * 해당 배송물건에 대해 배송완료 처리
     * @param deliverySeq
     * @param reqDto
     * @return
     */
    @Override
    public ProcessCompletedResDto processCompleted(Long deliverySeq, ProcessCompletedReqDto reqDto, MultipartFile attachFile) {
        Long driverSeq = CurrentUserContextHolder.get().getDriverSeq();
        LocalDateTime now = LocalDateTime.now();

        Delivery delivery = deliveryMapper.findById(deliverySeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DELIVERY));

        //배송기사 일치하는 지 확인
        if (!delivery.getDriverSeq().equals(driverSeq)) {
            throw new InvalidRequestException(ResponseCode.NOT_MATCH_DRIVER);
        }

        //배송중 상태인지 확인
        if (!DeliveryStatus.IN_DELIVERY.name().equals(delivery.getDeliveryStatusCd())) {
            throw new InvalidRequestException(ResponseCode.NOT_IN_DELIVERY);
        }

        //배송사진 파일 업로드
        Long newAttachSeq = null;
        if (!"Y".equals(reqDto.getShootingImpossibleYn())) {
            //파일업로드 체크
            if (attachFile == null) {
                throw new InvalidRequestException(ResponseCode.DELIVERY_PHOTO_FILE_NOT_FOUND);
            }

            try {
                newAttachSeq = uploadService.uploadFirstAttachFile(List.of(attachFile), AttachSubDirectory.DELIVERY_COMPLETED, driverSeq);
            } catch (Exception e) {
                throw new BusinessException(ResponseCode.FILE_UPLOAD_FAILED);
            }
        }


        //배송완료처리
        delivery.setDeliveryStatusCd(DeliveryStatus.COMPLETED.name());
        delivery.setDeliveryDatetime(now);
        delivery.setDeliveryPhotoAttachSeq(newAttachSeq);
        deliveryConverter.updateDelivery(delivery, reqDto);
        deliveryMapper.update(delivery);

        //이력생성
        this.createDevlieryHistory(delivery);

        //배송상태 변경 이벤트 전송(SSE)
        this.sendDeliveryEvent(delivery);

        //해당 배차의 모든 배송물량이 완료되었다면 dispatch의 상태도 배송완료로 Update
        Long dispatchSeq = delivery.getDispatchSeq();
        this.updateDispatchStatusToCompleted(dispatchSeq);


        //해당 배송기사의 배송물량 처리가 완료되었는지 조회 반환
        boolean isAllCompletedOfDriver = this.isAllCompletedOfDriver(dispatchSeq, driverSeq);

        ProcessCompletedResDto resDto = new ProcessCompletedResDto();
        resDto.setIsAllCompleted(isAllCompletedOfDriver);
        return resDto;

    }


    /**
     * 해당 배송물건에 대해 미배송, 미수거 처리
     * @param deliverySeq
     * @param reqDto
     * @return
     */
    @Override
    public ProcessUncompletedResDto processUncompleted(Long deliverySeq, ProcessUncompletedReqDto reqDto) {
        Long driverSeq = CurrentUserContextHolder.get().getDriverSeq();

        Delivery delivery = deliveryMapper.findById(deliverySeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DELIVERY));

        //배송기사 일치하는 지 확인
        if (!delivery.getDriverSeq().equals(driverSeq)) {
            throw new InvalidRequestException(ResponseCode.NOT_MATCH_DRIVER);
        }

        //배송중 상태인지 확인
        if (!DeliveryStatus.IN_DELIVERY.name().equals(delivery.getDeliveryStatusCd())) {
            throw new InvalidRequestException(ResponseCode.NOT_IN_DELIVERY);
        }

        //미배송처리
        delivery.setDeliveryStatusCd(DeliveryStatus.UNCOMPLETED.name());
        deliveryConverter.updateDelivery(delivery, reqDto);
        deliveryMapper.update(delivery);

        //이력생성
        this.createDevlieryHistory(delivery);

        //배송상태 변경 이벤트 전송(SSE)
        this.sendDeliveryEvent(delivery);

        //해당 배차의 모든 배송물량이 완료되었다면 Dispatch의 상태도 배송완료로 Update
        Long dispatchSeq = delivery.getDispatchSeq();
        this.updateDispatchStatusToCompleted(dispatchSeq);

        //해당 배송기사의 배송물량 처리가 완료되었는지 조회 반환
        boolean isAllCompletedOfDriver = this.isAllCompletedOfDriver(dispatchSeq, driverSeq);

        ProcessUncompletedResDto resDto = new ProcessUncompletedResDto();
        resDto.setIsAllCompleted(isAllCompletedOfDriver);
        return resDto;

    }


    /**
     * 배송기사의 배송물량 처리가 완료되었는지 조회
     * @param dispatchSeq
     * @param driverSeq
     * @return
     */
    private boolean isAllCompletedOfDriver(Long dispatchSeq, Long driverSeq) {
        return deliveryMapper.findByDispatchSeqAndDriverSeq(dispatchSeq, driverSeq)
                .stream()
                .allMatch(d -> StringUtils.containsAny(d.getDeliveryStatusCd(),
                        DeliveryStatus.COMPLETED.name(), DeliveryStatus.UNCOMPLETED.name()));
    }

    /**
     * 해당 배차의 모든 배송물량이 완료되었다면 dispatch의 상태도 배송완료로 Update
     * @param dispatchSeq
     */
    private void updateDispatchStatusToCompleted(Long dispatchSeq) {
        Long driverSeq = CurrentUserContextHolder.get().getDriverSeq();
        LocalDateTime now = LocalDateTime.now();

        boolean isAllCompleted = deliveryMapper.findByDispatchSeqAndDriverSeq(dispatchSeq, null)
                .stream()
                .allMatch(d -> StringUtils.containsAny(d.getDeliveryStatusCd(),
                        DeliveryStatus.COMPLETED.name(), DeliveryStatus.UNCOMPLETED.name()));

        if (isAllCompleted) {
            dispatchMapper.findById(dispatchSeq)
                    .ifPresent(dispatch -> {
                        dispatch.setDispatchStatusCd(DispatchStatus.COMPLETED.name());
                        dispatch.setModifiedAt(now);
                        dispatch.setModifierSeq(driverSeq);
                        dispatchMapper.update(dispatch);
                    });
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
     * 배송정보가 변경되었음을 알리는 SSE 이벤트 전송
     * @param delivery
     */
    private void sendDeliveryEvent(Delivery delivery) {
        DeliveryChangedEvent event = deliveryConverter.toDeliveryChangedEvent(delivery);
        deliveryEventNotifier.publish(delivery.getDriverSeq(), event);
    }


    /**
     * SSE (Server-Sent Events) 구독
     * 배송상태가 변경되면 EventStream을 통해 전달
     * @return
     */
    @Override
    public SseEmitter subscribe() {
        Long driverSeq = CurrentUserContextHolder.get().getDriverSeq();
        return sseHandler.subscribe(driverSeq);
    }

}
