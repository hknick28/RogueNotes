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
    displayNoteDetails();
    displayChildNotes();

    this.setVisible(true);
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

  private JButton createCloseWindowButton() {
    JButton closeButton = new JButton("Close");
    closeButton.addActionListener( e -> hideNotePanel() );
    return closeButton;
  }

  private JButton createBackButton() {
    JButton backButton = new JButton("Back");
    backButton.addActionListener( e -> System.out.println("Back button clicked!") );
    return backButton;
  }


  //middle component showing the description of notes
  //should be editable
  private void displayNoteDetails(){
    JPanel noteDetailsPanel = new JPanel();
    noteDetailsPanel.setBackground(Color.blue);

    //Scroll and editable note details
    noteDetailsPanel.setLayout(new BorderLayout());
    JTextArea detailsTextArea = new JTextArea(this.note.description());
    JScrollPane scrollPane = new JScrollPane(detailsTextArea);

    //add title
    JLabel title = new JLabel("Notes");
    title.setHorizontalAlignment(JLabel.CENTER);

    //save changes button
    JButton saveChanges = new JButton("Save Changes");
    saveChanges.addActionListener( e -> {
      String newText = detailsTextArea.getText();
      this.note.description(newText);
      System.out.println("New note description: " + newText);
    });

    //add components
    noteDetailsPanel.add(title, BorderLayout.NORTH);
    noteDetailsPanel.add(scrollPane, BorderLayout.CENTER);
    noteDetailsPanel.add(saveChanges, BorderLayout.SOUTH);

    this.add(noteDetailsPanel, BorderLayout.CENTER);

  }

  //bottom component showing the children of current note
  //should be editable
  private void displayChildNotes(){
    JPanel childNotesPanel = new JPanel();
    childNotesPanel.setLayout(new BorderLayout());

    JLabel title = new JLabel("Related Notes");
    title.setHorizontalAlignment(JLabel.CENTER);

    childNotesPanel.add(title, BorderLayout.NORTH);
    childNotesPanel.add(setupSubNotes(), BorderLayout.CENTER);
    childNotesPanel.add(childNoteButtons(), BorderLayout.SOUTH);

    this.add(childNotesPanel, BorderLayout.SOUTH);
  }

  private JPanel childNoteButtons() {
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(1, 1));
    buttonsPanel.setBackground(Color.yellow);

    JButton addNote = new JButton("Add New SubNote");

    addNote.addActionListener( e -> {
      NoteObject newNote;
      String name = JOptionPane.showInputDialog("Enter A Name:");
      if(name == null){ return; }
      newNote = new NoteObject(name, "", 0, 0);//default location for label on map

      this.note.addNoteObject(newNote);

      //update changes onto InfoPanel
    });

    buttonsPanel.add(addNote);

    return buttonsPanel;
  }

  private JScrollPane setupSubNotes() {
    DefaultListModel<String> subNoteListModel = new DefaultListModel<>();
    this.note.getChildrenNotes().forEach(note -> {subNoteListModel.addElement(note.name());});

    JList<String> notes = new JList<>(subNoteListModel);


    return new JScrollPane(notes);
  }

}
