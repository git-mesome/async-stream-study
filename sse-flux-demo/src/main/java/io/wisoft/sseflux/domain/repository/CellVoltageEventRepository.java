package io.wisoft.sseflux.domain.repository;

import io.wisoft.sseflux.domain.event.AbnormalCellVoltage;
import java.util.List;

public interface CellVoltageEventRepository {

  void save(AbnormalCellVoltage event);

  List<AbnormalCellVoltage> findAll();

}
