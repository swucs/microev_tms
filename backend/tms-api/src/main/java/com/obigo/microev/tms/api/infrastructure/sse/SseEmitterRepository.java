package com.obigo.microev.tms.api.infrastructure.sse;


import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {
    private final Map<Long, SseEmitters> sseEmitterMap = new ConcurrentHashMap<>();

    public void save(Long driverSeq, SseEmitter sseEmitter) {

        SseEmitters sseEmitters = sseEmitterMap.get(driverSeq);
        if (sseEmitters == null) {
            sseEmitters = new SseEmitters();
        }
        sseEmitters.add(sseEmitter);
        sseEmitterMap.put(driverSeq, sseEmitters);
    }

    public void deleteById(Long driverSeq) {
        sseEmitterMap.remove(driverSeq);
    }

    public SseEmitters findById(Long driverSeq) {
        return sseEmitterMap.get(driverSeq);
    }


}
