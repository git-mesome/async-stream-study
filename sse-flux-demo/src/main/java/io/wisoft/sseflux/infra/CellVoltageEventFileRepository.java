package io.wisoft.sseflux.infra;

import io.wisoft.sseflux.domain.CellVoltageEventRepository;
import io.wisoft.sseflux.domain.event.AbnormalCellVoltage;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CellVoltageEventFileRepository implements CellVoltageEventRepository {

  private static final String FILE_PATH = "cell-event.jsonc";
  private final FileHandler fileHandler;

  private CellVoltageEventFileRepository(FileHandler fileHandler) {
    this.fileHandler = fileHandler;
  }

  @Override
  public synchronized void save(AbnormalCellVoltage event) {
    fileHandler.writeEventToFile(FILE_PATH, event);
  }

  @Override
  public List<AbnormalCellVoltage> findAll() {
    return fileHandler.readEventsFromFile(FILE_PATH);
  }

}
