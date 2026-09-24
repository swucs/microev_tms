package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Delivery;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryCountByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatisticsByDayResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatisticsSummaryResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatusByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.WaybillResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface DeliveryMapper {

    int insert(Delivery delivery);

    int update(Delivery delivery);

    int updateDeliveryOrder(Delivery delivery);

    int delete(Long deliverySeq);

    Optional<Delivery> findById(@Param("deliverySeq") Long deliverySeq);

    List<Delivery> findByDispatchSeqAndDriverSeq(
            @Param("dispatchSeq") Long dispatchSeq,
            @Param("driverSeq") Long driverSeq
    );

    List<DeliveryResult> findByDispatchSeq(@Param("dispatchSeq") Long dispatchSeq);

    String findMaxTrackingNum(@Param("trackingNum") String trackingNum);

    String findTrackingNum(@Param("trackingNum") String trackingNum);

    Integer findMaxDeliveryOrder(@Param("dispatchSeq") Long dispatchSeq);

    DeliveryCountByDriverSeqResult findDeliveryCountByDriverSeq(
            @Param("driverSeq") Long driverSeq,
            @Param("dispatchSeq") Long dispatchSeq
    );

    int findCountProcessingDispatch(@Param("vehicleSeq") Long vehicleSeq);


    List<DeliveryStatusByDriverSeqResult> findDeliveryStatusByDriverSeq(
            @Param("driverSeq") Long driverSeq,
            @Param("dispatchSeq") Long dispatchSeq,
            @Param("deliveryAddr1") String deliveryAddr1
    );

    List<WaybillResult> findWaybills(
            @Param("driverSeq") Long driverSeq,
            @Param("dispatchSeq") Long dispatchSeq,
            @Param("deliveryAddr1") String deliveryAddr1
    );

    List<DeliveryStatisticsByDayResult> findDailyStatistics(
            @Param("driverSeq") Long driverSeq,
            @Param("deliveryStartDate") LocalDate deliveryStartDate,
            @Param("deliveryEndDate") LocalDate deliveryEndDate
    );

    DeliveryStatisticsSummaryResult findStatisticsSummary(
            @Param("driverSeq") Long driverSeq,
            @Param("deliveryStartDate") LocalDate deliveryStartDate,
            @Param("deliveryEndDate") LocalDate deliveryEndDate
    );

    String findDriverDeliveryStatus(
            @Param("driverSeq") Long driverSeq,
            @Param("dispatchSeq") Long dispatchSeq
    );

    int deleteByDispatchSeq(Long dispatchSeq);
}
