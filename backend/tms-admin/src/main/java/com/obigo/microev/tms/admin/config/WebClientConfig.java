package com.obigo.microev.tms.admin.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .clientConnector(clientHttpConnector())
                .build();
    }

    private ClientHttpConnector clientHttpConnector() {
        HttpClient httpClient = HttpClient.create(connectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 10초
                .responseTimeout(Duration.ofMillis(5000))
                .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        return new ReactorClientHttpConnector(httpClient);
    }


    private ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)					            // connection pool의 갯수
                .pendingAcquireTimeout(Duration.ofMillis(0))        //커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
                .pendingAcquireMaxCount(-1) 				        //커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
                .maxIdleTime(Duration.ofSeconds(10)) 		        //커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
                .maxLifeTime(Duration.ofSeconds(10)) 		        //커넥션 풀에서 커넥션을 유지하는 시간
                .build();
    }
}
