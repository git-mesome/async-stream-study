package io.wisoft.reactivemethod.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AsyncController {

  private static final Logger logger = LoggerFactory.getLogger(AsyncController.class);
  private final AsyncService service;

  public AsyncController(AsyncService service) {
    this.service = service;
  }

  @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<User> getUsers() {
    String[] users = {"git-mesome"};
    long startTime = System.currentTimeMillis();

    return Flux.fromArray(users)
        // 각 사용자에 대한 독립적인 비동기 작업
        .flatMap(user ->
            // 두 비동기 조회를 병렬로 실행 후 하나의 Flux로 합침
            Flux.merge(
                service.findUser(user)
                    .doOnNext(u1 -> logger.info("user1 진행시간 = {}ms",
                        System.currentTimeMillis() - startTime))
                ,
                service.findUserV2(user)
                    .doOnNext(u2 -> logger.info("user2 진행시간 = {}ms",
                        System.currentTimeMillis() - startTime))

            )

        )
        .doFinally(signalType ->
            logger.info("전체 진행시간 = {}ms", System.currentTimeMillis() - startTime)
        );
  }
}



