package io.wisoft.sseflux.interfaces;

import io.wisoft.sseflux.application.service.CellVoltageEventService;
import io.wisoft.sseflux.application.dto.request.ESSData;
import io.wisoft.sseflux.application.dto.response.CellMetrics;
import io.wisoft.sseflux.domain.event.AbnormalCellVoltage;
import io.wisoft.sseflux.infra.persistence.FileHandler;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

// todo monitoring system 확장

@RestController
public class CellVoltageController {

  private final CellVoltageEventService eventService;
  private final FileHandler fileHandler;

  public CellVoltageController(CellVoltageEventService eventService, FileHandler fileHandler) {
    this.eventService = eventService;
    this.fileHandler = fileHandler;
  }

  @GetMapping(value = "/cell-data", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<?>> getCellData() throws IOException {
    List<ESSData> essData = fileHandler.readEssData("ess.jsonc");
    List<CellMetrics> cellData = extractCellDataFromEss(essData);

    return Flux.merge(
        createCellMetricsStream(cellData),
        sendAbnormalCellVoltage(),
        sendHeartbeat()
    );
  }

  private Flux<ServerSentEvent<AbnormalCellVoltage>> sendAbnormalCellVoltage() throws IOException {
    return eventService.getAbnormalCellVoltageEvents()
        .map(abnormalEvent -> ServerSentEvent.<AbnormalCellVoltage>builder()
            .event("abnormal-cell-voltage-alert")
            .data(abnormalEvent)
            .build());
  }

  private Flux<ServerSentEvent<?>> createCellMetricsStream(List<CellMetrics> cellMetrics) {
    return Flux.fromIterable(cellMetrics)
        .delayElements(Duration.ofSeconds(1))
        .map(metrics -> {
          eventService.recordAbnormalCellVoltage(metrics.rackId(), metrics.moduleId(),
              metrics.cellVoltage());
          return ServerSentEvent.<CellMetrics>builder()
              .event("cell-update")
              .data(metrics)
              .build();
        });
  }

  private static Flux<ServerSentEvent<?>> sendHeartbeat() {
    return Flux.interval(Duration.ofSeconds(15))
        .map(seq -> ServerSentEvent.<Object>builder()
            .comment("keep-alive")
            .build());
  }

  private List<CellMetrics> extractCellDataFromEss(List<ESSData> essData) {
    return essData.stream()
        .flatMap(ess -> Optional.ofNullable(ess.rackData()).stream().flatMap(List::stream))
        .flatMap(rack -> Optional.ofNullable(rack.moduleData()).stream().flatMap(List::stream)
            .flatMap(module -> Optional.ofNullable(module.cellData()).stream().flatMap(List::stream)
                .map(cell -> new CellMetrics(
                    rack.rackId(),
                    module.moduleId(),
                    cell.cellVoltage(),
                    cell.cellTemperature()
                ))
            )
        )
        .toList();
  }

}



