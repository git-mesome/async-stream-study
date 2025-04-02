package io.wisoft.sseflux.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ESSData(
    long timestamp,
    @JsonProperty("ess_id")
    String essId,
    @JsonProperty("total_voltage")
    double totalVoltage,
    @JsonProperty("total_current")
    double totalCurrent,
    @JsonProperty("total_temperature")
    double totalTemperature,
    @JsonProperty("racks")
    List<RackData> rackData
) {

}