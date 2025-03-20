package io.wisoft.accessing.data.db.postgres.imsi;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class ImsiController {

  @GetMapping(value = "/stream-users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<User> streamUsersV2() {

    return Flux.concat(
        createBatch(1, 10, 1),
        createBatch(11, 20, 5),
        createBatch(31, 30, 10)
    );
  }

  private Flux<User> createBatch(int start, int count, int delaySeconds) {
    return Flux.range(start, count)
        .map(i -> new User("Name" + i, "user" + i + "@example.com"))
        .delayElements(Duration.ofMillis(100))  // 균등하게 분배 (선택)
        .delaySequence(Duration.ofSeconds(delaySeconds)); // delaySubscription보다 정확.
  }

  @GetMapping("/users")
  public List<User> getUsers() {
    // 예시:  총 60개의 User를 생성해서 반환 (스트리밍 아님!)
    return IntStream.rangeClosed(1, 60)
        .mapToObj(i -> new User("Name" + i, "user" + i + "@example.com"))
        .collect(Collectors.toList());
  }

}