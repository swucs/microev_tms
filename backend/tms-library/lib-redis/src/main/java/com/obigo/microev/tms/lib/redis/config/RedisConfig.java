package com.obigo.microev.tms.lib.redis.config;


import com.obigo.microev.tms.lib.redis.enumeration.RedisDBIndex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    /**
     * Redis 연결을 설정하기 위한 빈
     * */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(RedisDBIndex.TMS.getIndex());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }


    /**
     * Redis 데이터베이스에 대한 연산을 수행하는 데 사용되는 빈
     * */
    // PortalAdmin
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // RedisTemplate은 Redis 데이터베이스에 대한 연산을 수행하는 데 사용되는 빈입니다.
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // StringRedisSerializer를 사용하여 키와 값을 문자열 형태로 직렬화합니다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // RedisConnectionFactory를 설정하여 Redis 서버와의 연결을 관리합니다.
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

}

