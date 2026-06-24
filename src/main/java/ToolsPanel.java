import javax.swing.*;
import java.awt.*;

/**
 * Panel that provides access to tools for the users to utilize
 *
 * @author Hriday Patel
 */
public class ToolsPanel extends JPanel {

  public ToolsPanel(){
    this.setLayout(new GridLayout(1,3));
    this.setBackground(Color.white);
    this.setupButtons();
  }

  private void setupButtons(){
    this.add(new JButton("Load"));
    this.add(new JButton("Save"));
    this.add(new JButton("Favourites"));
  }
}

