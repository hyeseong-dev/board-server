package com.example.boardserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

public class RedisConfig {

    // Redis 서버의 호스트 주소
    @Value("${spring.data.redis.host}") private String redisHost;

    // Redis 서버의 포트 번호
    @Value("${spring.data.redis.port}") private int redisPort;

    // Redis 서버의 비밀번호
    @Value("${spring.data.redis.password}") private String redisPwd;

    // 캐시 만료 시간 (기본 설정)
    @Value("${expire.defaultTime}") private Long defaultTime;

    // ObjectMapper는 JSON 직렬화를 위한 설정입니다.
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 타임스탬프 대신 ISO-8601 날짜 형식을 사용
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 자바 8 날짜/시간 모듈을 등록하여 새로운 날짜/시간 API 지원
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    // RedisConnectionFactory는 Redis 서버에 연결하기 위한 설정입니다.
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(redisPwd);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    // RedisCacheManager는 캐시 관리를 위한 설정입니다.
    @Bean
    public RedisCacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory,
            ObjectMapper objectMapper
    ){
        // 기본 캐시 설정: null 값을 캐싱하지 않으며, TTL 설정
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(defaultTime))
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer())).serializeValuesWith(RedisSerializationContext.SerializationPair   // 키 직렬화: String 형식 사용
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));  // 값 직렬화: JSON 형식 사용

        // RedisCacheManagerBuilder를 사용하여 RedisCacheManager 생성
        return RedisCacheManager.RedisCacheManagerBuilder
                                .fromConnectionFactory(redisConnectionFactory)
                                .cacheDefaults(configuration)
                                .build();
    }
}
