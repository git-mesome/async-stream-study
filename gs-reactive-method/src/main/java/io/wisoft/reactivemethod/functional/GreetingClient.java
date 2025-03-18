package io.wisoft.reactivemethod.functional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GreetingClient {

  private final WebClient webClient;


  public GreetingClient(WebClient.Builder builder) {
    this.webClient = builder
        .baseUrl("http://localhost:8080")
        .build();
  }

  public Mono<String> getMessage() {
    return this.webClient.get()
        .uri("/hello")
        .accept(APPLICATION_JSON)
        .retrieve()
        .bodyToMono(Greeting.class)
        .map(Greeting::getMessage);
  }
}
