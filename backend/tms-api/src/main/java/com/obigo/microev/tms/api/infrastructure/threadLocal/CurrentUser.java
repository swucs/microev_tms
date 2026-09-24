package com.obigo.microev.tms.api.infrastructure.threadLocal;

import com.obigo.microev.tms.lib.redis.entity.DriverToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {
    private Long driverSeq;
    private String loginId;
    private String email;
    private String accessToken;

    public CurrentUser(DriverToken driverToken) {
        this.driverSeq = driverToken.getDriverSeq();
        this.loginId = driverToken.getLoginId();
        this.email = driverToken.getEmail();
        this.accessToken = driverToken.getAccessToken();
    }
}
