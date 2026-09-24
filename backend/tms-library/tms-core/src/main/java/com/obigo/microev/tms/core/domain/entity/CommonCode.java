package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommonCode {
    private Long comCodeSeq;
    private String comCodeGroupCd;
    private String comCodeCd;
    private String comCodeName;
    private Integer sortOrder;
    private String usageYn;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private LocalDateTime createdAt;
    private Long creatorSeq;
    private LocalDateTime modifiedAt;
    private Long modifierSeq;
}


