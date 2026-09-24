package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {
    Long insert(File file);

    List<File> findByAttachSeq(Long attachSeq);

    Optional<File> findById(Long fileSeq);
}
