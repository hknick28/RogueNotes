import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

public class NoteObject {
  private String name;
  private String description;

  private int x;
  private int y;

  private Point dragStart;

  private Consumer<MouseEvent> updateCanvas;

  private JLabel label;

  public NoteObject(String name, String description, int x, int y){
    this.name = name;
    this.description = description;
    this.x = x;
    this.y = y;

    setupLabel();

  }

  private void setupLabel() {
    int paddedWidth = 15;
    int paddedHeight = 10;

    // create the marker
    this.label = new JLabel(this.name);
    label.setForeground(new Color(50, 150, 250));
    label.setFont(new Font("Times New Roman", Font.BOLD, 20));

    Dimension size = label.getPreferredSize();
    label.setBounds(this.x(), this.y(), size.width + paddedWidth, size.height + paddedHeight);

    // add listener for dragging marker
    label.addMouseListener(setupClickSelection());
    label.addMouseMotionListener(setupDragListener());
  }

  public  String name(){return name;}
  public void name(String name){this.name=name;}

  public  String description(){return description;}
  public void description(String description){this.description=description;}

  public int x(){return x;}
  public void x(int x){this.x=x;}

  public int y(){return y;}
  public void y(int y){this.y=y;}

  public void draw(Canvas canvas){
    // Setup canvas as subscriber to observer
    this.dragEvent(e -> {canvas.repaint();});

    // Add the label to the canvas
    if(!canvas.contains(this)){
      canvas.add(label); canvas.repaint();};
  }

private MouseListener setupClickSelection() {
    return new MouseListener() {
      public void mousePressed(MouseEvent e) {
        if(!SwingUtilities.isLeftMouseButton(e)){return;}
        dragStart = e.getPoint();
      }

      @Override
      public void mouseClicked(MouseEvent e) {}

      @Override
      public void mouseReleased(MouseEvent e) {}

      @Override
      public void mouseEntered(MouseEvent e) {}

      @Override
      public void mouseExited(MouseEvent e) {}
    };
  }

  private MouseMotionListener setupDragListener(){
    return new MouseMotionListener() {

      @Override
      public void mouseDragged(MouseEvent e) {
          if(!SwingUtilities.isLeftMouseButton(e) || dragStart == null){return;}

          //how much mouse has moved
          int deltaX = e.getX() - dragStart.x;
          int deltaY = e.getY() - dragStart.y;

          //find new x, y pos
          int newX = label.getX() + deltaX;
          int newY = label.getY()+ deltaY;

          //tracking position
          NoteObject.this.x(newX);
          NoteObject.this.y(newY);

          //moving the label
          NoteObject.this.label.setLocation(newX, newY);

          if(updateCanvas != null){ updateCanvas.accept(e); }
        }

      @Override
      public void mouseMoved(MouseEvent e) {}
    };
  }

  /**
   * Observer to trigger redraw hen label has been dragged or moved
   *
   * @param canvas storing the label.
   */
    public void dragEvent(Consumer<MouseEvent> canvas){
      this.updateCanvas = canvas;
    }

}
