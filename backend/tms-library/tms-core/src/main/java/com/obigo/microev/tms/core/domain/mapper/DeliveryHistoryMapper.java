package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Delivery;
import com.obigo.microev.tms.core.domain.entity.DeliveryHistory;
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
public interface DeliveryHistoryMapper {
    int insert(DeliveryHistory deliveryHistory);
}
