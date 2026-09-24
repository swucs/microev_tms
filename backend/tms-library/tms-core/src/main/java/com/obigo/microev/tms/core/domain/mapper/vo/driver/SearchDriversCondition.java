package com.obigo.microev.tms.core.domain.mapper.vo.driver;

import lombok.Data;

@Data
public class SearchDriversCondition {
    private String driverName;
    private String centerName;
    private String statusCd;
}
