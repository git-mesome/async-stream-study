package io.wisoft.asyncmethod;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration.WebFluxConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class AsyncController {

  private static final Logger logger = LoggerFactory.getLogger(AsyncController.class);

  private final GitHubLookupService gitHubLookupService;
  private final Executor executor;

  @Autowired
  public AsyncController(GitHubLookupService gitHubLookupService, Executor executor) {
    this.gitHubLookupService = gitHubLookupService;
    this.executor = executor;
  }

  @GetMapping("/users")
  public ResponseBodyEmitter getUsers() {
    ResponseBodyEmitter emitter = new ResponseBodyEmitter(600000L); // 타임아웃 설정 (60초)
    String[] users = {"git-mesome", "CloudFactory", "Spring-Projects"};
    long startTime = System.currentTimeMillis();

    for (String user : users) {
      CompletableFuture<Void> future1 = CompletableFuture.runAsync(
          () -> {
            try {
              User result = gitHubLookupService.findUser(user);
              emitter.send(result);
              logger.info("future2 진행시간 = {}ms", System.currentTimeMillis() - startTime);
            } catch (Exception e) {
              emitter.completeWithError(e);
            }
          }
          , executor);
      CompletableFuture.allOf(future1)
          .thenRun(() -> { // thenRun 안에 전체 진행시간 로깅
            logger.info("전체 진행시간 = {}ms", System.currentTimeMillis() - startTime);
            emitter.complete(); // 수정: emitter 완료
          })
          .exceptionally(ex -> {
            emitter.completeWithError(ex); // 예외 발생 시 처리
            return null;
          });
    }

    return emitter;
  }
}

