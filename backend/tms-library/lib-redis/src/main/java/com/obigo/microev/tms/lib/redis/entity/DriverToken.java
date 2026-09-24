package com.obigo.microev.tms.lib.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * 사용자 토큰 : 만료시간 2일
 */
@RedisHash(value = "driverToken", timeToLive = 60 * 60 * 24 * 1)
@Builder
@Getter
public class DriverToken {

    @Id
    private String accessToken;
    private Long driverSeq;
    private String loginId;
    private String email;
}
