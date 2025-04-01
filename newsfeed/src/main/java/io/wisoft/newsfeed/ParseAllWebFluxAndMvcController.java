package io.wisoft.newsfeed;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@Controller
public class ParseAllWebFluxAndMvcController {

  // webflux
  @GetMapping(value = "/news", produces = "text/event-stream")
  public Flux<ServerSentEvent<List<Article>>> news() throws IOException {
    Resource resource = new ClassPathResource("news.json");
    Path path = resource.getFile().toPath();
    String json = Files.readString(path);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    News news = mapper.readValue(json, News.class);

    return Flux.fromIterable(news.articles())
        .window(3)
        .delayElements(Duration.ofSeconds(1))
        .flatMap(Flux::collectList)
        .map(articles -> ServerSentEvent.<List<Article>>builder()
            .event("news")
            .data(articles)
            .build());


  }

  private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();

  // web mvc
  @GetMapping(value = "/news-emitter")
  public SseEmitter newsEmitter() throws IOException {
    SseEmitter emitter = new SseEmitter();
    Resource resource = new ClassPathResource("news.json");
    Path path = resource.getFile().toPath();
    String json = Files.readString(path);

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    News news = mapper.readValue(json, News.class);

    List<List<Article>> articleBatches = news.articles().stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
              List<List<Article>> batches = new ArrayList<>();
              for (int i = 0; i < list.size(); i += 3) {
                batches.add(list.subList(i, Math.min(i + 3, list.size())));
              }
              return batches;
            }
        ));

    nonBlockingService.execute(() -> {
      try {
        for (List<Article> articles : articleBatches) {
          try {
            emitter.send(articles);
            Thread.sleep(1000);
          } catch (Exception e) {
            emitter.completeWithError(e);
          }
        }
        emitter.complete();
      } catch (Exception e) {
        emitter.completeWithError(e);
      }
    });
    return emitter;
  }
}