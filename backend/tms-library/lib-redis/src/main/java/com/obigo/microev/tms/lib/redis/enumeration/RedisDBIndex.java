package com.obigo.microev.tms.lib.redis.enumeration;

import lombok.Getter;

@Getter
public enum RedisDBIndex {

    TMS(9);

    private final int index;
    RedisDBIndex(int index) {
        this.index = index;
    }
}
