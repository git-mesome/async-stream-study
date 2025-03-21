package io.wisoft.accessing.data.db.redis.sync;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoffeeService {

  private final CoffeeRepository coffeeRepository;

  @Autowired
  public CoffeeService(CoffeeRepository coffeeRepository) {
    this.coffeeRepository = coffeeRepository;
  }

  public Optional<Coffee> getCoffeeById(String id) {
    return coffeeRepository.findById(id);
  }

  public List<Coffee> getAllCoffees() {
    return StreamSupport.stream(
        coffeeRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }
}
