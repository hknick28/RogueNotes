import javax.swing.*;
import java.awt.*;

public class NoteInfo extends JPanel {
  private NoteObject note;
  private JPanel main_panel;

  public NoteInfo(NoteObject noteObject, JPanel panel) {
    this.main_panel = panel;
    this.note = noteObject;
    this.setBackground(Color.orange);
    this.setLayout(new BorderLayout());

    setupHeader();
    this.setVisible(true);
  }

  private JButton createCloseWindowButton() {
    JButton closeButton = new JButton("Close");
    closeButton.addActionListener( e -> hideNotePanel() );
    return closeButton;
  }

  private void hideNotePanel(){ this.setVisible(false); main_panel.remove(this); }



  private void setupHeader(){
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BorderLayout());

    JLabel titleLabel = new JLabel("Note Info: " + this.note.name());
    titleLabel.setHorizontalAlignment(JLabel.CENTER);

    headerPanel.add(createBackButton(), BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);
    headerPanel.add(createCloseWindowButton(), BorderLayout.EAST);

    this.add(headerPanel, BorderLayout.NORTH);
  }

  private JButton createBackButton() {
    JButton backButton = new JButton("Close");
    backButton.addActionListener( e -> System.out.println("Back button clicked!") );
    return backButton;
  }

}
