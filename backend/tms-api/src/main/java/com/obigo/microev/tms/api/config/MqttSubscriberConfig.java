package com.obigo.microev.tms.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.api.infrastructure.sse.SseHandler;
import com.obigo.microev.tms.lib.config.MqttConfig;
import com.obigo.microev.tms.lib.vo.MqttDeliveryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqttSubscriberConfig {

    private final ObjectMapper objectMapper;
    private final MqttPahoClientFactory mqttClientFactory;
    private final SseHandler sseHandler;


    @Value("${mqtt.url}")
    private String url;

    //동일한 클라이언트 아이디로 여러개의 클라이언트가 접속하면 서버에서 오류가 발생함.
    private static final String MQTT_CLIENT_ID_SUB = MqttClient.generateClientId();

    /**
     * 배송 정보 메시지 수신을 위한 채널을 구성
     * @return
     */
    @Bean
    public MessageProducer inboundChannelForDelivery() { // inboundChannel 어댑터
        var adapter = new MqttPahoMessageDrivenChannelAdapter(
                url,
                MQTT_CLIENT_ID_SUB,
                mqttClientFactory,
                MqttConfig.MQTT_TOPIC_DELIVERY + "/#"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannelForDelivery());
        return adapter;
    }

    /**
     * 배송 정보 구독 채널 생성
     * 해당 채널을 구독한 모든 핸들러에게 브로드 캐스트
     * @return
     */
    @Bean
    public MessageChannel mqttInputChannelForDelivery() { // MQTT 구독 채널 생성
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannelForDelivery") // MQTT 구독 핸들러
    public MessageHandler messageHandlerForDelivery() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) {
                log.debug("Topic: {}", message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
                log.debug("Received message: {}", message.getPayload());

                try {
                    MqttDeliveryMessage mqttDeliveryMessage = objectMapper.readValue((String) message.getPayload(), MqttDeliveryMessage.class);
                    String ssePayload = objectMapper.writeValueAsString(mqttDeliveryMessage);
                    sseHandler.sendSseEvent(mqttDeliveryMessage.getDriverSeq(), "deliveryEvent", ssePayload);
                } catch (Exception e) {
                    log.error("messageHandlerForDelivery Error", e);
                }
            }
        };
    }
}
