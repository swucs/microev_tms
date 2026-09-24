package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Center;
import com.obigo.microev.tms.core.domain.mapper.vo.center.CenterDetailResult;
import com.obigo.microev.tms.core.domain.mapper.vo.center.SearchCentersCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.center.SearchCentersResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CenterMapper {
    long insert(Center center);

    long update(Center center);

    long deleteById(Long centerSeq);

    Center findById(Long centerSeq);

    CenterDetailResult findDetailById(Long centerSeq);

    List<SearchCentersResult> findByConditions(SearchCentersCondition condition);
}
