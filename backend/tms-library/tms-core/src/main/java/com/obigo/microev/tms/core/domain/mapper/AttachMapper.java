package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Attach;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AttachMapper {
    Long insert(Attach attach);

    Optional<Object> findById(Long attachSeq);
}
