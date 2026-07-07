import javax.swing.*;
import java.awt.*;


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

    tools.setOnLoadImg(file -> canvas.loadImg(file));

    this.add(tools,  BorderLayout.NORTH);
    this.add(panel,   BorderLayout.CENTER);
    this.setVisible(true);
  }

}
