package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.mapper.vo.driver.SearchDriversCondition;
import com.obigo.microev.tms.core.domain.entity.Driver;
import com.obigo.microev.tms.core.domain.mapper.vo.driver.SearchDriversResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DriverMapper {
    long insert(Driver driver);

    long update(Driver driver);

    long deleteById(Long driverSeq);

    Optional<Driver> findById(Long driverSeq);

    Optional<Driver> findByLoginId(String loginId);

    Optional<Driver> findByExtSystemLinkedId(String extSystemLinkedId);

    List<SearchDriversResult> findByConditions(SearchDriversCondition condition);
}
