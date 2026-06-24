import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NoteObject {
  private String name;
  private String description;
  private int x;
  private int y;


  public NoteObject(String name, String description, int x, int y){
    this.name = name;
    this.description = description;
    this.x = x;
    this.y = y;
  }

  public  String name(){return name;}
  public void name(String name){this.name=name;}

  public  String description(){return description;}
  public void description(String description){this.description=description;}

  public int x(){return x;}
  public void x(int x){this.x=x;}

  public int y(){return y;}
  public void y(int y){this.y=y;}

  public void draw(JPanel canvas){
    int paddedWidth = 15;
    int paddedHeight = 10;

    // create the marker
    JLabel label = new JLabel(name);
    label.setForeground(new Color(50, 150, 250));
    label.setFont(new Font("Times New Roman", Font.BOLD, 20));

    Dimension size = label.getPreferredSize();
    label.setBounds(this.x(), this.y(), size.width + paddedWidth, size.height + paddedHeight);

    // add listener for dragging marker
    MouseAdapter dragListener = setupDragListener();

    // Add the label to the canvas
    canvas.add(label);
    canvas.repaint();
  }

  private MouseAdapter setupDragListener() {
    return new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
      }

      public void mouseDragged(MouseEvent e) {}
    };
  }
}
