import javax.swing.*;
import java.awt.*;

public class NoteInfo extends JPanel {
  private NoteObject note;
  public NoteInfo(NoteObject noteObject) {
    this.note = noteObject;
    this.setBackground(Color.orange);
    this.setLayout(new BorderLayout());
    this.setVisible(true);
  }
}
