package io.wisoft.accessing.data.db.redis.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CoffeeService2 {

  private final CoffeeRepository2 coffeeRepository2;

  @Autowired
  public CoffeeService2(CoffeeRepository2 coffeeRepository2) {
    this.coffeeRepository2 = coffeeRepository2;
  }

  public Flux<Coffee2> getAllCoffees() {
    return coffeeRepository2.findAll();
  }
}
