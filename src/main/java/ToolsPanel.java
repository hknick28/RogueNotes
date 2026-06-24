import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

/**
 * Panel that provides access to tools for the users to utilize
 *
 * @author Hriday Patel
 */
public class ToolsPanel extends JPanel {

  private Consumer<File> onLoadImg;
  public ToolsPanel(){
    this.setLayout(new GridLayout(1,3));
    this.setBackground(Color.white);
    this.setupButtons();
  }

  private void setupButtons(){
    this.add(loadButton());
    this.add(new JButton("Save"){});
    this.add(new JButton("Favourites"));
  }
  private JButton loadButton(){
    JButton load = new JButton("Load");
    load.addActionListener(e->{
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg");
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this);
      if(returnVal == JFileChooser.APPROVE_OPTION) {

        File file = chooser.getSelectedFile();

        if(onLoadImg!=null){
          //trigger if observer is present
          onLoadImg.accept(file);
        }
      }
    });
    return load;
  }

  /**
   * Add load file observer.
   * @param handler to be triggered when loading a file.
   */
  public void setOnLoadImg(Consumer<File> handler){
    this.onLoadImg = handler;
  }
}

