import java.util.ArrayList;
import java.util.List;

// Represents a single label/note on the canvas
public class LabelData {
  private String name;
  private String description;
  private int x;
  private int y;
  private List<LabelData> subnotes = new ArrayList<>();

  public LabelData() {}
  public LabelData(String name, String description, int x, int y, List<LabelData> subnotes) {
    this.name = name;
    this.description = description;
    this.x = x;
    this.y = y;
    this.subnotes = subnotes;
  }

  // Getters and Setters
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public int getX() { return x; }
  public void setX(int x) { this.x = x; }

  public int getY() { return y; }
  public void setY(int y) { this.y = y; }

  public List<LabelData> getSubnotes() { return subnotes; }
  public void setSubnotes(List<LabelData> subnotes) { this.subnotes = subnotes; }
}