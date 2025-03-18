package io.wisoft.reactivemethod;

import io.wisoft.reactivemethod.functional.GreetingClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GsReactiveMethodApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(GsReactiveMethodApplication.class,
        args);

    GreetingClient greetingClient = context.getBean(GreetingClient.class);
    System.out.println(">> message = " + greetingClient.getMessage().block());
  }

}
