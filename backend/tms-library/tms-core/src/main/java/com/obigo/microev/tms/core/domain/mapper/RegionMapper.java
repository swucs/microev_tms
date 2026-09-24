package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Region;
import com.obigo.microev.tms.core.domain.entity.RegionRange;
import com.obigo.microev.tms.core.domain.mapper.vo.region.RegionsFindByConditionsResult;
import com.obigo.microev.tms.core.domain.mapper.vo.region.SearchRegionsCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RegionMapper {
    long insert(Region region);

    long insertRegionRange(RegionRange regionRange);

    long update(Region region);

    long delete(Long regionSeq);

    long deleteRegionRangeByRegionId(Long regionSeq);

    Region findById(Long regionSeq);

    RegionRange findRegionRangeById(RegionRange regionRange);

    List<RegionRange> findRegionRangesByRegionSeq(Long regionSeq);

    List<RegionsFindByConditionsResult> findByConditions(SearchRegionsCondition condition);
}
