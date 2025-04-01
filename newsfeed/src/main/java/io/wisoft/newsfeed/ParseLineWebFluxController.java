package io.wisoft.newsfeed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/server-events")
public class ParseLineWebFluxController {

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<String>> getEvents() throws IOException {

    Stream<String> lines = Files.lines(Path.of(
        "/Users/minseo/workspace/journal/async-stream-study/newsfeed/src/main/resources/news.json"));

    AtomicInteger counter = new AtomicInteger(1);

    return Flux.fromStream(lines)
        .filter(line -> !line.isBlank())
        .map(line ->
            ServerSentEvent.<String>builder()
                .id(String.valueOf(counter.getAndIncrement()))
                .data(line)
                .event("lineEvent")
                .retry(Duration.ofSeconds(1))
                .build())
        .delayElements(Duration.ofSeconds(1));
  }
}
