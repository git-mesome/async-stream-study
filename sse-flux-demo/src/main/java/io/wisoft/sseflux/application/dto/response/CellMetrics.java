package io.wisoft.sseflux.application.dto.response;

public record CellMetrics(
    String rackId,
    String moduleId,
    double cellVoltage,
    double cellTemperature) {

}