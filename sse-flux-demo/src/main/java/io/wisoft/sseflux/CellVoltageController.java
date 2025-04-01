package io.wisoft.sseflux;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.sseflux.dto.CellData;
import io.wisoft.sseflux.dto.CellMetrics;
import io.wisoft.sseflux.dto.ESSData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class CellVoltageController {

  @GetMapping(value = "/cell-data", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<?>> getCellData() throws IOException {
    List<ESSData> essData = readEssDataFromJson();
    List<CellData> cellData = extractCellDataFromEss(essData);

    Flux<ServerSentEvent<?>> celldataFlux = createCellMetricsStream(cellData);

    Flux<ServerSentEvent<?>> heartbeatFlux = Flux.interval(Duration.ofSeconds(15))
        .map(seq -> ServerSentEvent.<Object>builder()
            .comment("keep-alive")
            .build());

    return Flux.merge(celldataFlux, heartbeatFlux);
  }

  private static List<ESSData> readEssDataFromJson() throws IOException {
    Resource resource = new ClassPathResource("ess.json");
    Path path = resource.getFile().toPath();
    String json = Files.readString(path);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper.readValue(json, new TypeReference<List<ESSData>>() {
    });
  }

  private static List<CellData> extractCellDataFromEss(List<ESSData> essData) {
    return essData.stream()
        .flatMap(ess -> Optional.ofNullable(ess.rackData()).stream().flatMap(List::stream))
        .flatMap(rack -> Optional.ofNullable(rack.moduleData()).stream().flatMap(List::stream))
        .flatMap(module -> Optional.ofNullable(module.cellData()).stream().flatMap(List::stream))
        .toList();
  }

  private static Flux<ServerSentEvent<?>> createCellMetricsStream(List<CellData> cellData) {
    return Flux.fromIterable(cellData)
        .delayElements(Duration.ofSeconds(1))
        .map(cell -> {
          CellMetrics metrics = new CellMetrics(cell.cellVoltage(), cell.cellTemperature());

          return ServerSentEvent.<CellMetrics>builder()
              .event("cell-update")
              .data(metrics)
              .build();
        });
  }



}
