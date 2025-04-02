package io.wisoft.sseflux.domain.event;

public class AbnormalCellVoltage {
  private final String rackId;
  private final String moduleId;
  private final double cellVoltage;


  public AbnormalCellVoltage(String rackId, String moduleId, double cellVoltage) {
    this.rackId = rackId;
    this.moduleId = moduleId;
    this.cellVoltage = cellVoltage;
  }

  public String getRackId() {
    return rackId;
  }

  public String getModuleId() {
    return moduleId;
  }

  public double getCellVoltage() {
    return cellVoltage;
  }

}
