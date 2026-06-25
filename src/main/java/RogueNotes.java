import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RogueNotes extends JFrame {
  public RogueNotes() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(800, 600);
    this.setLocationRelativeTo(null);

    ToolsPanel tools = new ToolsPanel();
    Canvas canvas = new Canvas();

    tools.setOnLoadImg(file -> canvas.loadImg(file));

    this.add(tools,  BorderLayout.NORTH);
    this.add(canvas,   BorderLayout.CENTER);
    this.setVisible(true);
  }

}
