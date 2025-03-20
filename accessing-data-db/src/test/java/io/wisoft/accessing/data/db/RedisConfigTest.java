package io.wisoft.accessing.data.db;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@SpringBootTest // Spring Boot 컨텍스트 로드
public class RedisConfigTest {

    @Autowired
    private RedisConnectionFactory connectionFactory; // 자동 주입

    @Test
    void testRedisConfigurationFromApplicationYml() {
        // 1. connectionFactory가 LettuceConnectionFactory인지 확인 (Lettuce 사용 여부)
        assertTrue(connectionFactory instanceof LettuceConnectionFactory);


    }
}