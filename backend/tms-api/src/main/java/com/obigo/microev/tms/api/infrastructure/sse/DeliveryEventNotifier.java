package com.obigo.microev.tms.api.infrastructure.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryEventNotifier {

    private final ObjectMapper objectMapper;
    private final SseHandler sseHandler;

    public void publish(Long driverSeq, DeliveryChangedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            sseHandler.sendSseEvent(driverSeq, "deliveryEvent", payload);
        } catch (JsonProcessingException e) {
            log.error("DeliveryEventNotifier publish Error", e);
        }
    }
}
