package io.wisoft.accessing.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Test;

public class RedisConnectionTest {

    @Test
    void testRedisConnection() {
        // Redis 서버 정보 (호스트, 포트)
        String host = "127.0.0.1"; // 또는 "localhost"
        int port = 61902;        // application.yml에 설정한 포트

        // RedisURI 생성
        RedisURI redisURI = RedisURI.builder()
                .withHost(host)
                .withPort(port)
                // .withPassword("your_password") // 필요한 경우 패스워드 설정
                .build();

        // RedisClient 생성
        RedisClient redisClient = RedisClient.create(redisURI);

        // try-with-resources를 사용하여 연결 자동 닫기
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {

            // 동기(Sync) API 사용
            RedisCommands<String, String> syncCommands = connection.sync();

            // PING 명령어 실행
            String pong = syncCommands.ping();

            // 응답 확인
            assertNotNull(pong);
            assertEquals("PONG", pong);
            System.out.println("Redis connection test successful: " + pong);

        } catch (Exception e) {
             System.err.println("Redis Connection Fail");
             e.printStackTrace();
        } finally {
            // RedisClient 종료 (리소스 해제)
            redisClient.shutdown();
        }
    }
}