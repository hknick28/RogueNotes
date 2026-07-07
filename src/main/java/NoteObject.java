import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class NoteObject {
  private String name;
  private String description;

  private ArrayList<NoteObject> childrenNotes;

  private int x;
  private int y;

  private Point dragStart;

  private Consumer<MouseEvent> updateCanvas;
  private Consumer<MouseEvent> displayNoteInfo;

  private JLabel label;

  public NoteObject(String name, String description, int x, int y){
    this.name = name;
    this.description = description;

    childrenNotes = new ArrayList<>();

    this.x = x;
    this.y = y;
  }

  public void setupLabel() {
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
    if(canvas.contains(this)){return;}

    // Setup canvas as subscriber to observer
    this.dragEvent(e -> {canvas.repaint();});
    this.doubleCLick(e -> {
      canvas.displayInfo(NoteObject.this);
    });

    // Add the label to the canvas
      canvas.add(label);
      canvas.repaint();
  }

private MouseListener setupClickSelection() {
    return new MouseListener() {
      public void mousePressed(MouseEvent e) {
        if(!SwingUtilities.isLeftMouseButton(e)){return;}
        dragStart = e.getPoint();
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() != 2){return;}
        System.out.println("double clicked");
        displayNoteInfo.accept(e);
      }

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
    private void dragEvent(Consumer<MouseEvent> canvas){
      this.updateCanvas = canvas;
    }

    private void doubleCLick(Consumer<MouseEvent> canvas){
      this.displayNoteInfo = canvas;
    }



    public void addNoteObject(NoteObject object){
      assert object != null;
      childrenNotes.add(object);
    }

    public List<NoteObject> getChildrenNotes(){
      return Collections.unmodifiableList(this.childrenNotes);
    }

}
