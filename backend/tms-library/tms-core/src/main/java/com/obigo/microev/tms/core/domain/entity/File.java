package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {
    private Long fileSeq;
    private Long attachSeq;
    private String fileFullPath;
    private String originalFileName;
    private int sortOrder;
}
