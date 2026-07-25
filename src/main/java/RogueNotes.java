import javax.swing.*;
import java.awt.*;
import java.io.File;


public class RogueNotes extends JFrame {
  public RogueNotes() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(800, 600);
    this.setLocationRelativeTo(null);

    ToolsPanel tools = new ToolsPanel();

    JPanel panel = new JPanel();

    Canvas canvas = new Canvas(panel);

    panel.setLayout(new BorderLayout());

    panel.add(canvas, BorderLayout.CENTER);

    FileHandler fileHandler = new FileHandler();

    tools.setOnLoadRequested(() -> fileHandler.openFile(canvas));
    tools.setOnSaveRequested(() -> fileHandler.saveProject(canvas));

    this.add(tools,  BorderLayout.NORTH);
    this.add(panel,   BorderLayout.CENTER);
    this.setVisible(true);
  }

}
