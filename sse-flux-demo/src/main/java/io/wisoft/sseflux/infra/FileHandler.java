package io.wisoft.sseflux.infra;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.sseflux.application.dto.request.ESSData;
import io.wisoft.sseflux.domain.event.AbnormalCellVoltage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class FileHandler {

  private final ObjectMapper mapper = new ObjectMapper();
  private final ConcurrentMap<String, List<AbnormalCellVoltage>> cache = new ConcurrentHashMap<>();

  public FileHandler() {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
  }

  public List<AbnormalCellVoltage> readEventsFromFile(String filePath) {
    if (cache.containsKey(filePath)) {
      return cache.get(filePath);
    }
    try {
      Path path = Paths.get(filePath);
      String json = Files.readString(path);
      List<AbnormalCellVoltage> events = mapper.readValue(
          json,
          new TypeReference<List<AbnormalCellVoltage>>() {
          });
      cache.put(filePath, events);
      return events;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void writeEventToFile(String filePath, AbnormalCellVoltage event) {
    try {
      Path path = Paths.get(filePath);
      String jsonLine = mapper.writeValueAsString(event);
      Files.writeString(path, jsonLine + "\n", CREATE, APPEND);
      cache.remove(filePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }


  public List<ESSData> readEssData(String filePath) throws IOException {
    Path path = new ClassPathResource("ess.jsonc").getFile().toPath();
    String json = Files.readString(path);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    return mapper.readValue(json, new TypeReference<List<ESSData>>() {
    });
  }

}
