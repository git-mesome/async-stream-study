package io.wisoft.accessing.data.db.redis.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CoffeeConfiguration {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  ReactiveRedisOperations<String, Coffee2> redisOperations(ReactiveRedisConnectionFactory factory) {
    Jackson2JsonRedisSerializer<Coffee2> serializer
        = new Jackson2JsonRedisSerializer<>(Coffee2.class);

    RedisSerializationContext.RedisSerializationContextBuilder<String, Coffee2> builder
        = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

    RedisSerializationContext<String, Coffee2> context = builder
        .value(serializer)
        .build();

    return new ReactiveRedisTemplate<>(factory, context); // ReactiveRedisTemplate 사용
  }

//  @Bean
//  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() { // 반환 타입 ReactiveRedisConnectionFactory 로 변경
//    RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
//    redisConfiguration.setHostName(host);
//    redisConfiguration.setPort(port);
//    return new LettuceConnectionFactory(redisConfiguration); // Reactive LettuceConnectionFactory (동일 클래스 사용)
//  }

}
