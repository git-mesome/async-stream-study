package io.wisoft.accessing.data;

import io.wisoft.accessing.data.r2dbc.Customer;
import io.wisoft.accessing.data.r2dbc.CustomerRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "io.wisoft.accessing.data.r2dbc")
public class AccessingDataApplication {

  private static final Logger logger = LoggerFactory.getLogger(AccessingDataApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(AccessingDataApplication.class, args);
  }

  @Bean
  public CommandLineRunner demo(CustomerRepository repository) {
    return args -> {
      repository.saveAll(List.of(new Customer("Jack", "Bauer"),
              new Customer("Chloe", "O'Brian"),
              new Customer("Kim", "Bauer"),
              new Customer("David", "Palmer"),
              new Customer("Michelle", "Dessler")
          ))
          // Flux의 모든 데이터가 저장될 때까지 현재 스레드 차단
          .thenMany(repository.findAll()) // 저장 후 모든 고객 조회
          .doOnNext(customer -> logger.info("findAll(): " + customer.toString()))
          .thenMany(repository.findById(2L))  // ID로 조회
          .doOnNext(customer -> logger.info("findById(2L): " + customer.toString()))
          .thenMany(repository.findByLastName("Bauer")) // lastName으로 조회
          .doOnNext(bauer -> logger.info("findByLastName('Bauer'): " + bauer.toString()))
          .then() // 모든 작업이 완료될 때까지 기다림
          .subscribe(
              null, // onNext:  데이터 처리 (여기서는 필요 없음)
              err -> logger.error("An error occurred: ", err),  // onError: 에러 처리
              () -> logger.info("Demo finished") // onComplete:  모든 작업 완료 후 로그 출력
          );
    };
  }

}
