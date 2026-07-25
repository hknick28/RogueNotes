import java.util.ArrayList;
import java.util.List;

public class CanvasData {
  private String backgroundColorHex;
  private String backgroundImagePath; // Path to original image on disk
  private String base64ImageData;    // Optional: Embed image data directly in file
  private List<LabelData> labels = new ArrayList<>();

  public CanvasData() {}

  // Getters and Setters
  public String getBackgroundColorHex() { return backgroundColorHex; }
  public void setBackgroundColorHex(String backgroundColorHex) { this.backgroundColorHex = backgroundColorHex; }

  public String getBackgroundImagePath() { return backgroundImagePath; }
  public void setBackgroundImagePath(String backgroundImagePath) { this.backgroundImagePath = backgroundImagePath; }

  public String getBase64ImageData() { return base64ImageData; }
  public void setBase64ImageData(String base64ImageData) { this.base64ImageData = base64ImageData; }

  public List<LabelData> getLabels() { return labels; }
  public void setLabels(List<LabelData> labels) { this.labels = labels; }
}