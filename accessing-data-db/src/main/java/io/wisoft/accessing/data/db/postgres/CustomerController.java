package io.wisoft.accessing.data.db.postgres;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
public class CustomerController {

  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  private final CustomerService customerService;

  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // MediaType 변경 및 엔드포인트 경로 변경 (sse-stream -> stream)
  public ResponseBodyEmitter stream() { // ResponseBodyEmitter 로 변경
    ResponseBodyEmitter emitter = new ResponseBodyEmitter(600000L); // ResponseBodyEmitter 로 변경
    long startTime = System.currentTimeMillis();

    Flux<Customer> mergedFlux = Flux.merge(
        customerService.streamData().doOnNext(c -> logger.info("future1 진행시간 = {}ms", System.currentTimeMillis() - startTime)).doOnError(e -> logger.error("future1 flux error", e)).doOnComplete(() -> logger.info("future1 completed")),
        customerService.streamData().doOnNext(c -> logger.info("future2 진행시간 = {}ms", System.currentTimeMillis() - startTime)).doOnError(e -> logger.error("future2 flux error", e)).doOnComplete(() -> logger.info("future2 completed"))
    );

    mergedFlux
        .publishOn(Schedulers.boundedElastic())
        .subscribe(
            customer -> {
              try {
                emitter.send(customer, MediaType.APPLICATION_JSON); // SseEmitter.event() 제거 및 MediaType.APPLICATION_JSON 유지
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

  @GetMapping(value = "/sse-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter sseStream() {
    SseEmitter emitter = new SseEmitter(600000L);
    long startTime = System.currentTimeMillis();

    Flux<Customer> mergedFlux = Flux.merge(
        customerService.streamData().doOnNext(c -> logger.info("future1 진행시간 = {}ms", System.currentTimeMillis() - startTime)).doOnError(e -> logger.error("future1 flux error", e)).doOnComplete(() -> logger.info("future1 completed")),
        customerService.streamData().doOnNext(c -> logger.info("future2 진행시간 = {}ms", System.currentTimeMillis() - startTime)).doOnError(e -> logger.error("future2 flux error", e)).doOnComplete(() -> logger.info("future2 completed"))
    );

    mergedFlux
        .publishOn(Schedulers.boundedElastic())
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

