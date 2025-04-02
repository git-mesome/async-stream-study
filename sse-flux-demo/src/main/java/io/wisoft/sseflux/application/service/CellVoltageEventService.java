package io.wisoft.sseflux.application.service;

import io.wisoft.sseflux.domain.event.AbnormalCellVoltage;
import java.io.IOException;
import reactor.core.publisher.Flux;

public interface CellVoltageEventService {
  void recordAbnormalCellVoltage(String rackId, String moduleId, double cellVoltage);
  Flux<AbnormalCellVoltage> getAbnormalCellVoltageEvents() throws IOException;
}