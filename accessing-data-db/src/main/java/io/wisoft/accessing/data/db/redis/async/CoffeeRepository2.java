package io.wisoft.accessing.data.db.redis.async;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class CoffeeRepository2 {

  private final ReactiveRedisOperations<String, Coffee2> coffeeOps;

  @Autowired
  public CoffeeRepository2(ReactiveRedisOperations<String, Coffee2> coffeeOps) {
    this.coffeeOps = coffeeOps;
  }

  public Flux<Coffee2> findAll() {
    return coffeeOps.keys("*")
        .window(2)
        .delayElements(Duration.ofSeconds(1))
        .flatMap(keyWindow ->
            keyWindow.flatMap(key -> coffeeOps.opsForValue().get(key)));
  }
}