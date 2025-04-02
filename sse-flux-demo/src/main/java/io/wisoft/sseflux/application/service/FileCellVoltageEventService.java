package io.wisoft.sseflux.application.service;

import io.wisoft.sseflux.domain.repository.CellVoltageEventRepository;
import io.wisoft.sseflux.domain.event.AbnormalCellVoltage;
import io.wisoft.sseflux.infra.persistence.CellVoltageEventFileRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class FileCellVoltageEventService implements CellVoltageEventService {

  private final CellVoltageEventRepository eventRepository;
  private final Sinks.Many<AbnormalCellVoltage> abnormalEventSink;

  public FileCellVoltageEventService(CellVoltageEventFileRepository eventRepository) {
    this.eventRepository = eventRepository;
    this.abnormalEventSink = Sinks.many().multicast().onBackpressureBuffer();
  }

  @Override
  public void recordAbnormalCellVoltage(String rackId, String moduleId, double cellVoltage) {
    if (cellVoltage >= 3.9 || cellVoltage <= 3.5) {
      AbnormalCellVoltage event = new AbnormalCellVoltage(rackId, moduleId, cellVoltage);
      eventRepository.save(event);
      abnormalEventSink.tryEmitNext(event);
    }
  }

  @Override
  public Flux<AbnormalCellVoltage> getAbnormalCellVoltageEvents() {
    return abnormalEventSink.asFlux();
  }
}