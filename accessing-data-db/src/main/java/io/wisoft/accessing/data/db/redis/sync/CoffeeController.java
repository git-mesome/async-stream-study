package io.wisoft.accessing.data.db.redis.sync;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coffees")
public class CoffeeController {

  private final CoffeeService coffeeService;

  @Autowired
  public CoffeeController(CoffeeService coffeeService) {
    this.coffeeService = coffeeService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Coffee> getCoffeeById(@PathVariable String id) {
    Optional<Coffee> coffee = coffeeService.getCoffeeById(id);

    return coffee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping()
  public ResponseEntity<List<Coffee>> getAllCoffees() {

    List<Coffee> coffees = coffeeService.getAllCoffees();
    return new ResponseEntity<>(coffees, HttpStatus.OK);
  }

}