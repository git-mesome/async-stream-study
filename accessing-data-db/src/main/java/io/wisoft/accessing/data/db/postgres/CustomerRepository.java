package io.wisoft.accessing.data.db.postgres;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

  @Query("SELECT * FROM my_data ORDER BY id") // 필요에 따라 정렬 조건 추가
  Flux<Customer> findAllData();

}
