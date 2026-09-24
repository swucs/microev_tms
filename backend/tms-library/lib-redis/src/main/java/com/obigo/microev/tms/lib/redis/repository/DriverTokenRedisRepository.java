package com.obigo.microev.tms.lib.redis.repository;

import com.obigo.microev.tms.lib.redis.entity.DriverToken;
import org.springframework.data.repository.CrudRepository;

public interface DriverTokenRedisRepository extends CrudRepository<DriverToken, String> {
}
