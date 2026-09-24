package com.obigo.microev.tms.core.domain.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class S3File {
    private String fileFullPath;
    private String originalFileName;
    private int sortOrder;
}
