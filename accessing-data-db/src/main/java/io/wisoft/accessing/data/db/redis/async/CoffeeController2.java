package io.wisoft.accessing.data.db.redis.async;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
public class CoffeeController2 {

  private final CoffeeService2 coffeeService;
  private static final Logger logger = LoggerFactory.getLogger(CoffeeController2.class);

  public CoffeeController2(CoffeeService2 coffeeService) {
    this.coffeeService = coffeeService;
  }

  @GetMapping(value = "/redis-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter getAllCoffees() {
    SseEmitter emitter = new SseEmitter();
    long startTime = System.currentTimeMillis();

    Flux<Coffee2> flux = Flux.merge(
        coffeeService.getAllCoffees()
            .doOnNext(
                c -> logger.info("coffee1 진행시간 = {}ms", System.currentTimeMillis() - startTime))
            .doOnError(e -> logger.error("coffee1 flux error", e))
            .doOnComplete(() -> logger.info("coffee1 completed"))
        , coffeeService.getAllCoffees()
            .doOnNext(
                c -> logger.info("coffee2 진행시간 = {}ms", System.currentTimeMillis() - startTime))
            .doOnError(e -> logger.error("coffee2 flux error", e))
            .doOnComplete(() -> logger.info("coffee2 completed"))
    );

    flux.publishOn(Schedulers.boundedElastic())
        .subscribe(
            customer -> {
              try {
                emitter.send(SseEmitter.event() // SseEmitter.event() 사용
                    .name("customer-event") // 이벤트 이름 (선택 사항)
                    .data(customer, MediaType.APPLICATION_JSON));
              } catch (IOException e) {
                emitter.completeWithError(e);
              }
            },
            emitter::completeWithError,
            () -> {
              logger.info("Total time: {}ms", System.currentTimeMillis() - startTime);
              emitter.complete();
            }
        );
    return emitter;
  }

}
