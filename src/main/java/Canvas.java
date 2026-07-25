import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

  public Canvas(JPanel parent) {
    main_panel = parent;
    noteObjects = new ArrayList<>();
    noteStack = new Stack<>();

    currentNote = null;

    this.setBackground(Color.white);
    this.setLayout(null);
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

    note.setupLabel();
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
  }

  public void clearCanvas() {
    this.removeAll();
    this.noteObjects.clear();
    this.noteStack.clear();
    this.currentNote = null;
    this.image = null;
    this.revalidate();
    this.repaint();
  }
}

