package io.wisoft.reactivemethod.functional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingRouterTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testHello() {
    webTestClient
        .get()
        .uri("/hello")
        .exchange()
        .expectStatus().isOk()
        .expectBody(Greeting.class)
        .value(greeting -> {
          assertThat(greeting.getMessage()).isEqualTo("Hello, Reactive Spring!");
        });
  }



}