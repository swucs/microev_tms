package com.obigo.microev.tms.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.obigo.microev.tms.core.domain.mapper")
public class MybatisConfig {
}
