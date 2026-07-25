import javax.swing.*;
import java.awt.*;

/**
 * Panel that provides access to tools for the users to utilize
 *
 * @author Hriday Patel
 */
public class ToolsPanel extends JPanel {

  private Runnable onLoadRequested;
  private Runnable onSaveRequested;

  public ToolsPanel(){
    this.setLayout(new GridLayout(1,3));
    this.setBackground(Color.white);
    this.setupButtons();
  }

  private void setupButtons(){
    this.add(loadButton());
    this.add(saveButton());
    this.add(new JButton("Favourites"));
  }

  private JButton loadButton(){
    JButton load = new JButton("Load");
    load.addActionListener(e -> {
      System.out.println("Loading...");
      if (onLoadRequested != null) {
        onLoadRequested.run();
      }
    });
    return load;
  }

  private JButton saveButton(){
    JButton save = new JButton("Save");
    save.addActionListener(e -> {
      if (onSaveRequested != null) {
        onSaveRequested.run();
      }
    });
    return save;
  }

  public void setOnLoadRequested(Runnable handler){
    this.onLoadRequested = handler;
  }

  public void setOnSaveRequested(Runnable handler){
    this.onSaveRequested = handler;
  }
}