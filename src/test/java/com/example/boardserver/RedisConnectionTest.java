package com.example.boardserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    public void testRedisConnection() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.ping()).isEqualTo("PONG");
        }
    }
}
