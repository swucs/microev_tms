package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class Dispatch {
    private Long dispatchSeq;
    private Long centerSeq;
    private String dispatchName;
    private LocalDate deliveryDate;
    private String dispatchStatusCd;
    private LocalDateTime createdAt;
    private Long creatorSeq;
    private LocalDateTime modifiedAt;
    private Long modifierSeq;
}
