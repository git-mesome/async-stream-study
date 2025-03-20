package io.wisoft.accessing.data.r2dbc;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Flux<Customer> streamData() {
    return customerRepository.findAll()
        .window(2)
        .delayElements(Duration.ofSeconds(1))
        .flatMap(chunk -> chunk)
        .doOnNext(customer -> { // 각 Customer 객체가 도착할 때마다 실행
          logger.info("Received customer at : {}", customer);
        });
  }
}
