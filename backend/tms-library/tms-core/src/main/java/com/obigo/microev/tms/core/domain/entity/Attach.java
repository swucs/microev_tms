package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Attach {
    private Long attachSeq;
    private Long creatorSeq;
    private LocalDateTime createdAt;
}
