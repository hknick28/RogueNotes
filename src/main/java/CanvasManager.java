import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class CanvasManager {
  private final ObjectMapper mapper;

  public CanvasManager() {
    this.mapper = new ObjectMapper();

    //temp fix, set max file size
    StreamReadConstraints constraints = StreamReadConstraints.builder()
            .maxStringLength(100_000_000)
            .build();

    this.mapper.getFactory().setStreamReadConstraints(constraints);
    // Formats JSON nicely so it's human-readable
    this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  // Save Canvas Data to JSON file
  public void saveCanvas(CanvasData canvasData, File file) throws IOException {
    mapper.writeValue(file, canvasData);
  }

  // Load Canvas Data from JSON file
  public CanvasData loadCanvas(File file) throws IOException {
    return mapper.readValue(file, CanvasData.class);
  }
}