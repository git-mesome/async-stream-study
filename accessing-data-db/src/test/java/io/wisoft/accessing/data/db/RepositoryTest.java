package io.wisoft.accessing.data.db;

import static org.assertj.core.api.Assertions.assertThat;

import io.wisoft.accessing.data.db.redis.Coffee;
import io.wisoft.accessing.data.db.redis.CoffeeRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {

  private final CoffeeRepository coffeeRepository;

  @Autowired
  public RepositoryTest(CoffeeRepository coffeeRepository) {
    this.coffeeRepository = coffeeRepository;
  }

//  @Autowired
//  private RedisTemplate<String, Object> redisTemplate; // RedisTemplate 주입

  @BeforeEach
  void setUp() {
    // 테스트 전에 Redis 데이터를 깨끗하게 지웁니다. (선택 사항)
    coffeeRepository.deleteAll();
  }
  @Test
  void saveAndFindById() {
    // Given
    Coffee coffee = new Coffee(1L, "Americano");

    // When
    coffeeRepository.save(coffee);
    Optional<Coffee> foundCoffee = coffeeRepository.findById("1");

    // Then
    assertThat(foundCoffee).isPresent();
    assertThat(foundCoffee.get().getId()).isEqualTo("1");
    assertThat(foundCoffee.get().getName()).isEqualTo("Americano");
  }


}
