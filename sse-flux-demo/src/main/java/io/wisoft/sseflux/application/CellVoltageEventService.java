package io.wisoft.sseflux.application;

import io.wisoft.sseflux.domain.CellVoltageEventRepository;
import io.wisoft.sseflux.domain.event.AbnormalCellVoltage;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CellVoltageEventService {

  private final CellVoltageEventRepository eventRepository;

  public CellVoltageEventService(CellVoltageEventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public void recordAbnormalCellVoltage(String rackId, String moduleId, double cellVoltage){
    if (cellVoltage >= 3.85 || cellVoltage < 3.6) {
      AbnormalCellVoltage event = new AbnormalCellVoltage(rackId, moduleId, cellVoltage);
      eventRepository.save(event);
    }
  }

  public List<AbnormalCellVoltage> getAllEvents() throws IOException {
    return eventRepository.findAll();
  }
}
