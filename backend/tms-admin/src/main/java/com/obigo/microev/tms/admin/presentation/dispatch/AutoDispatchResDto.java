package com.obigo.microev.tms.admin.presentation.dispatch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutoDispatchResDto {
    private Long regionSeq;
    private String regionName;
    private Long vehicleSeq;
    private String vehicleName;
}
