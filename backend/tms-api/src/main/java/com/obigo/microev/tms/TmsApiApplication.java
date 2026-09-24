package com.obigo.microev.tms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class TmsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmsApiApplication.class, args);
        log.info("Application started successfully.");
    }
}
