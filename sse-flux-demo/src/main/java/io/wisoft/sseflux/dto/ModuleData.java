package io.wisoft.sseflux.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ModuleData(
    @JsonProperty("module_id")
    String moduleId,
    @JsonProperty("module_voltage")
    double moduleVoltage,
    @JsonProperty("module_current")
    double moduleCurrent,
    @JsonProperty("cells")
    List<CellData> cellData
) {

}