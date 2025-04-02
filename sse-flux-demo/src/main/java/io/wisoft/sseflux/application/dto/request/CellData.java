package io.wisoft.sseflux.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CellData(
    @JsonProperty("cell_id")
    long cellId,
    @JsonProperty("cell_voltage")
    double cellVoltage,
    @JsonProperty("cell_temperature")
    double cellTemperature,
    @JsonProperty("balancing_status")
    int balancingStatus,
    @JsonProperty("cell_current")
    double cellCurrent
) {

}