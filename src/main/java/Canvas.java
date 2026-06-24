import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Main canvas to store images and objects
 *
 * @author Hriday Patel
 */
public class Canvas extends JPanel {
  private BufferedImage image = null;//none by default

  public Canvas() {
    this.setBackground(Color.white);
    this.setLayout(new BorderLayout());
  }

  /**
   * Load png and jpg files onto the canvas.
   * Images are loaded as the background.
   * @param img to be loaded.
   */
  public void loadImg(File img){
    try {
      this.image = ImageIO.read(img);
    } catch (IOException e){
      JOptionPane.showMessageDialog(this, "Could not open image file.", "Error", JOptionPane.ERROR_MESSAGE);    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    //draw img
    if(image == null) {return;}
    g.drawImage(image, 0, 0, this);
  }
}
