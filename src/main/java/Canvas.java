import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import  java.util.Stack;
import java.util.stream.Stream;

/**
 * Main canvas to store images and objects
 *
 * @author Hriday Patel
 */
public class Canvas extends JPanel {
  private BufferedImage image = null;//none by default
  private ArrayList<NoteObject> noteObjects;
  private final JPanel main_panel;
  private Stack<NoteObject> noteStack;
  private  NoteObject currentNote;

  //vars for panning and zoom
  private double scale = 1.0;
  private double translateX = 0;
  private double translateY = 0;
  private Point lastDragPoint;

  public Canvas(JPanel parent) {
    main_panel = parent;
    noteObjects = new ArrayList<>();
    noteStack = new Stack<>();

    currentNote = null;

    this.setBackground(Color.white);
    this.setLayout(null);
    initMouseListeners();
  }

  // mouse listener for right click
  void initMouseListeners() {


    //create note
    MouseAdapter adapter = new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        //Create note
        if (SwingUtilities.isRightMouseButton(e)) {
          Point p = screenToCanvasCoords(e.getPoint());
          creatObjectDialog(p);
          return;
        }
        //start dragging
        if (SwingUtilities.isLeftMouseButton(e)) {
          lastDragPoint = e.getPoint();
          return;
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        //reset drag point
        if (SwingUtilities.isLeftMouseButton(e)) {
          lastDragPoint = null;
        }
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e) || lastDragPoint == null) { return; }

        //calculate drag offsets
        int dx =  e.getX() - lastDragPoint.x;
        int dy =  e.getY() - lastDragPoint.y;

        translateX+=dx;
        translateY+=dy;

        lastDragPoint = e.getPoint();

        updateLabelPositionAndSize();
        repaint();
      }


      @Override
      public void mouseWheelMoved(MouseWheelEvent e){
        double zoomFactor = 1.1;
        double oldScale = scale;

        // nagative means zoom-in, positive means zoom out
        if(e.getWheelRotation() < 0){
          scale*=zoomFactor;
        } else { scale/=zoomFactor; }

        // set zooming bounds
        scale = Math.max(0.3, Math.min(scale, 4.0));

        // ensure zoom happens at mouse location
        Point p = e.getPoint();

        translateX = p.x - (p.x - translateX) * (scale/oldScale);
        translateY = p.y - (p.y - translateY) * (scale/oldScale);

        updateLabelPositionAndSize();
        repaint();
      }
    };

    this.addMouseListener(adapter);
    this.addMouseMotionListener(adapter);
    this.addMouseWheelListener(adapter);
  }


  private Point screenToCanvasCoords(Point point) {
    int canvasX = (int) ((point.x - translateX) / scale);
    int canvasY = (int) ((point.y - translateY) / scale);
    return new Point(canvasX, canvasY);
  }


  void updateLabelPositionAndSize() {
    for(NoteObject note : noteObjects) {
      JLabel label = note.label();
      if(label == null){ continue; }

      //calculate relative pixel position
      int screenX = (int) (note.x() * scale + translateX);
      int screenY = (int) (note.y() * scale + translateY);

      //scale fonts
      float baseFontSize = 20.0f;
      float scaledFontSize = (float) (baseFontSize * scale);
      label.setFont(label.getFont().deriveFont(Math.max(8.0f, scaledFontSize)));

      //update bounds relative to new font sizes
      Dimension size = label.getPreferredSize();
      label.setBounds(screenX, screenY, size.width + 10, size.height + 5);
    }
  }

  private void creatObjectDialog(Point p) {
    NoteObject note;
    String name = JOptionPane.showInputDialog("Enter A Name:");
    String description = JOptionPane.showInputDialog("Enter A Description:");

    if(name == null || description == null){return;}

    note = new NoteObject(name, description, p.x, p.y);

    note.setupLabel();
    note.draw(this);
    noteObjects.add(note);
  }


  public double scale(){ return scale; }

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

    //cast ot 2d graphics
    Graphics2D g2d = (Graphics2D) g.create();

    // Create and apply the transform matrix
    java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
    transform.translate(translateX, translateY); // Apply Panning
    transform.scale(scale, scale);               // Apply Zooming

    g2d.transform(transform);

    // Draw background image using the transformed coordinates
    g2d.drawImage(image, 0, 0, this);


    //redraw all objects on canvas
    noteObjects.stream().forEach(note -> note.draw(this));

  }

  public boolean contains(NoteObject note){
    return this.noteObjects.contains(note);
  }

  public void displayInfo(NoteObject noteObject) {
    this.currentNote = noteObject;

    //cover the right half of window
    int panelWidth = main_panel.getWidth() / 2;
    int panelHeight = main_panel.getHeight();

    NoteInfo infoPanel = new NoteInfo(currentNote, main_panel, this);
    infoPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));

    main_panel.add(infoPanel, BorderLayout.EAST);

    //sit ontop of canvas contents
    main_panel.setComponentZOrder(infoPanel, 0);
    main_panel.revalidate();
    main_panel.repaint();
  }

  /**
   * Roll back to the previous note in the stack. If there is no previous note, then do nothing.
   * Make current note null, will be reset in displayInfo() method.
   */
  public void displayPreviousNote(){
    if(noteStack.isEmpty()){return;}// there is no previous note

    NoteObject newNote = noteStack.pop();//remove from stack

    this.currentNote = null;//make null
    displayInfo(newNote);
  }

  public void clearNoteStack(){
    noteStack.clear();
  }

  public boolean noteStackEmpty(){
    return noteStack.isEmpty();
  }

  public void addSubNoteHistory(){ noteStack.push(currentNote); }



  //for laoding and saving
  public BufferedImage getBackgroundImage() {
    return this.image;
  }

  public void setBackgroundImage(BufferedImage bgImage) {
    this.image = bgImage;
    this.repaint();
  }

  public ArrayList<NoteObject> getNoteObjects() {
    return this.noteObjects;
  }

  public void addLoadedNote(NoteObject note) {
    note.draw(this);
    this.noteObjects.add(note);
    updateLabelPositionAndSize();
  }

  public void clearCanvas() {
    this.removeAll();
    this.noteObjects.clear();
    this.noteStack.clear();
    this.currentNote = null;
    this.image = null;
    this.scale = 1.0;
    this.translateX = 0;
    this.translateY = 0;
    this.revalidate();
    this.repaint();
  }
}

