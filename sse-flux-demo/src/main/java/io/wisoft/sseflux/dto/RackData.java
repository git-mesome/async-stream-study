package io.wisoft.sseflux.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RackData(
    @JsonProperty("rack_id")
    String rackId,
    @JsonProperty("modules")
    List<ModuleData> moduleData
) {}