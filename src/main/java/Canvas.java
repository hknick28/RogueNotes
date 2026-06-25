import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Main canvas to store images and objects
 *
 * @author Hriday Patel
 */
public class Canvas extends JPanel {
  private BufferedImage image = null;//none by default
  private ArrayList<NoteObject> noteObjects;

  public Canvas() {
    noteObjects = new ArrayList<>();
    this.setBackground(Color.white);
    this.setLayout(new BorderLayout());
    initMouseListener();
  }

  // mouse listener for right click
  void initMouseListener(){
    this.addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        // right click triggers NoteObject creation
        if(!SwingUtilities.isRightMouseButton(e)){return;}
        creatObjectDialog(e.getPoint());

      }
    });
  }

  private void creatObjectDialog(Point p) {
    NoteObject note;
    String name = JOptionPane.showInputDialog("Enter A Name:");
    String description = JOptionPane.showInputDialog("Enter A Description:");

    if(name == null || description == null){return;}

    note = new NoteObject(name, description, p.x, p.y);
    note.draw(this);
    noteObjects.add(note);

  }



  /**
   * Load png and jpg files onto the canvas.
   * Images are loaded as the background.
   * @param img to be loaded.
   */
  public void loadImg(File img){
    try {
      this.image = ImageIO.read(img);
      this.repaint();
    } catch (IOException e){
      JOptionPane.showMessageDialog(this, "Could not open image file.", "Error", JOptionPane.ERROR_MESSAGE);    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    //draw img
    if(image == null) {return;}
    g.drawImage(image, 0, 0, this);

    noteObjects.stream().forEach(note -> note.draw(this));

  }

  public boolean contains(NoteObject note){

    boolean check = this.noteObjects.contains(note);

    System.out.println(check);
    return check;
  }
}
