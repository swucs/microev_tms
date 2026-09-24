package com.obigo.microev.tms.lib.publisher;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.lib.config.MqttConfig;
import com.obigo.microev.tms.lib.vo.MqttDeliveryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttPublisher {
    private final MqttConfig.OutboundGateway outboundGateway;
    private final ObjectMapper objectMapper;

    /**
     * 배송 정보가 변경된 이벤트를 전송한다.
     * topic : tms/delivery/{driverSeq}
     * @param driverSeq
     * @param payload
     */
    public void sendChangedDelivery(long driverSeq, MqttDeliveryMessage payload) {
        try {
            final String topic = MqttConfig.MQTT_TOPIC_DELIVERY + "/" + driverSeq;
            String payloadString = objectMapper.writeValueAsString(payload);
            outboundGateway.sendToMqtt(payloadString, topic);
        } catch (JsonProcessingException e) {
            log.error("sendChangedDelivery Error", e);
        }
    }
}
