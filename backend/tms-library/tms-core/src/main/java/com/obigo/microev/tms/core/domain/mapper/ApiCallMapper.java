package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.ApiCall;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ApiCallMapper {
    long insert(ApiCall apiCall);
}
