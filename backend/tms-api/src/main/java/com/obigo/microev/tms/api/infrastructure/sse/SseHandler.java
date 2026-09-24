package com.obigo.microev.tms.api.infrastructure.sse;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
@RequiredArgsConstructor
public class SseHandler {
    private static final Long DEFAULT_SSE_EMITTER_TIMEOUT = 60L * 1000 * 60;

    private final SseEmitterRepository sseEmitterRepository;

    /**
     * SSE (Server-Sent Events) 구독
     * 배송상태가 변경되면 EventStream을 통해 전달
     * @return
     */
    public SseEmitter subscribe(Long driverSeq) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_SSE_EMITTER_TIMEOUT);

        sseEmitter.onCompletion(() -> {
            log.info("SseEmitter complete : {}", driverSeq);
            sseEmitterRepository.deleteById(driverSeq);
        });

        sseEmitter.onTimeout(() -> {
            log.info("SseEmitter timeout : {}", driverSeq);
            sseEmitterRepository.deleteById(driverSeq);
        });

        sseEmitterRepository.save(driverSeq, sseEmitter);
        this.sendSseEvent(driverSeq, "deliveryEvent", "EventStream Created");
        return sseEmitter;
    }


    /**
     * SSE (Server-Sent Events) 이벤트 전송
     * @param driverSeq
     * @param eventName
     * @param message
     */
    public void sendSseEvent(Long driverSeq, String eventName, String message) {
        SseEmitters sseEmitters = sseEmitterRepository.findById(driverSeq);

        if (sseEmitters != null) {
            sseEmitters.send(
                    SseEmitter.event().id(String.valueOf(driverSeq))
                    .name(eventName)
                    .data(message)
            );
            log.info("SseEmitter Send 완료 : driverSeq [{}], eventName [{}], message [{}]", driverSeq, eventName, message);
        }
    }
}
