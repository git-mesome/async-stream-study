package io.wisoft.accessing.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "io.wisoft.accessing.data.r2dbc")
public class AccessingDataApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccessingDataApplication.class, args);
  }

}
