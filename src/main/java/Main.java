import javax.swing.*;
import java.awt.*;

public class Main {
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.add(new ToolsPanel(),  BorderLayout.NORTH);
    frame.add(new Canvas(),   BorderLayout.CENTER);
    frame.setVisible(true);
  }
}
