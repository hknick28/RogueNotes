import javax.swing.*;
import java.awt.*;

public class NoteInfo extends JPanel {
  private NoteObject note;
  private JPanel main_panel;
  public NoteInfo(NoteObject noteObject, JPanel panel) {
    this.main_panel = panel;
    this.note = noteObject;
    this.setBackground(Color.orange);
    this.setLayout(new GridLayout(1, 3));

    createCloseWindowButton();

    this.setVisible(true);
  }

  private void createCloseWindowButton(){
    JButton closeButton = new JButton("Close");
    closeButton.addActionListener( e -> hideNotePanel() );
    this.add(closeButton);
  }

  public void hideNotePanel(){ this.setVisible(false); main_panel.remove(this); }



}
