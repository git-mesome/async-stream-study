package io.wisoft.reactivemethod.annotation;

import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AsyncService {

  private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);
  List<User> users = List.of(
      createUser("User1_"),
      createUser("User2_"),
      createUser("User3_")
  );

  public Flux<User> findUser(String user) {
    logger.info("Looking up v1 " + user);

    // Mock User 객체 리스트 생성 (실제로는 DB에서 조회하거나 다른 로직을 수행)

    // Flux.fromIterable()을 사용하여 List를 Flux로 변환
    return Flux.fromIterable(users)
        .delayElements(Duration.ofSeconds(1));
  }

  public Flux<User> findUserV2(String user) {
    logger.info("Looking up v2 " + user);

    return Flux.fromIterable(users)
        .delayElements(Duration.ofSeconds(1));
  }

  private User createUser(String name) {
    User user = new User();
    user.setName(name);

    return user;
  }
}
