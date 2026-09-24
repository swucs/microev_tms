package com.obigo.microev.tms.api.infrastructure.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SseEmitters {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void add(SseEmitter emitter) {
        this.emitters.add(emitter);
    }

    public void send(SseEmitter.SseEventBuilder builder) {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(builder);
            } catch (Exception e) {
                emitter.completeWithError(e);
                failedEmitters.add(emitter);
            }
        });
        this.emitters.removeAll(failedEmitters);
    }
}
