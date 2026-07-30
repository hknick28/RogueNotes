import java.util.ArrayList;
import java.util.List;

public class CanvasData {
  private String backgroundColorHex;
  private List<LabelData> labels = new ArrayList<>();

  public CanvasData() {}

  public String getBackgroundColorHex() { return backgroundColorHex; }
  public void setBackgroundColorHex(String backgroundColorHex) { this.backgroundColorHex = backgroundColorHex; }

  public List<LabelData> getLabels() { return labels; }
  public void setLabels(List<LabelData> labels) { this.labels = labels; }
}