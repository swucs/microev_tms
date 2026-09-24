package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Dispatch;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.DispatchDetailResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.SearchDispatchesCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.SearchDispatchesResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface DispatchMapper {
    List<SearchDispatchesResult> findByConditions(SearchDispatchesCondition condition);

    DispatchDetailResult findDetailById(long dispatchSeq);

    Optional<Dispatch> findById(long dispatchSeq);

    int insert(Dispatch dispatch);

    int update(Dispatch dispatch);

    DispatchDetailResult findByDriverSeq(
            @Param("driverSeq") long driverSeq
            , @Param("deliveryDate") LocalDate deliveryDate
    );

    int delete(long dispatchSeq);
}
