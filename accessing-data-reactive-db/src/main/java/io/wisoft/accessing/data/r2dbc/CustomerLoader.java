package io.wisoft.accessing.data.r2dbc;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@DependsOnDatabaseInitialization
public class CustomerLoader {

  private static final Logger logger = LoggerFactory.getLogger(CustomerLoader.class);

  private final CustomerRepository repository;

  @Autowired
  public CustomerLoader(CustomerRepository repository) {
    this.repository = repository;
  }

  @PostConstruct
  public void init() {
    repository.count()
        .filter(count -> count == 0)
        .flatMapMany(v ->
            repository.saveAll(Flux.just(
                new Customer("Jack", "Bauer"),
                new Customer("Chloe", "O'Brian"),
                new Customer("Kim", "Bauer"),
                new Customer("David", "Palmer"),
                new Customer("Michelle", "Dessler")
            ))
        ).subscribe(
            null, // onNext:  데이터 처리 (여기서는 필요 없음)
            err -> logger.error("An error occurred: ", err),  // onError: 에러 처리
            () -> logger.info("Demo finished") // onComplete:  모든 작업 완료 후 로그 출력
        );

  }
}
