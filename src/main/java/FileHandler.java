import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandler {

  private final CanvasManager canvasManager = new CanvasManager();

  public void saveProject(Canvas canvas) {
    System.out.println("Saving project archive...");
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Save Canvas Project");

    // File filter for custom archive extension
    FileNameExtensionFilter filter = new FileNameExtensionFilter("RogueNotes Project (*.rgp)", "rgp");
    chooser.setFileFilter(filter);

    int result = chooser.showSaveDialog(canvas);
    if (result == JFileChooser.APPROVE_OPTION) {
      File targetFile = chooser.getSelectedFile();

      // Ensure file ends with .rgp extension
      if (!targetFile.getName().toLowerCase().endsWith(".rgp")) {
        targetFile = new File(targetFile.getAbsolutePath() + ".rgp");
      }

      try {
        CanvasData data = new CanvasData();

        // Convert Note Objects to Data DTOs
        List<LabelData> labelDataList = new ArrayList<>();
        for (NoteObject note : canvas.getNoteObjects()) {
          labelDataList.add(note.toData());
        }
        data.setLabels(labelDataList);

        // Retrieve background image from Canvas
        BufferedImage bgImage = canvas.getBackgroundImage();

        // Save metadata and image into single ZIP archive
        canvasManager.saveCanvas(data, bgImage, targetFile);

        JOptionPane.showMessageDialog(canvas, "Saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(canvas, "Failed to save: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void openFile(Canvas canvasPanel) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Open Project or Image");

    // Allow opening compressed project archives or standalone images
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("RogueNotes Project (*.rgp, *.zip)", "rgp", "zip"));
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));

    int result = fileChooser.showOpenDialog(canvasPanel);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String fileName = selectedFile.getName().toLowerCase();

      try {
        if (fileName.endsWith(".rgp") || fileName.endsWith(".zip")) {
          loadZipProject(selectedFile, canvasPanel);
        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
          loadImageAsBackground(selectedFile, canvasPanel);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(canvasPanel, "Failed to open file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void loadZipProject(File file, Canvas canvasPanel) throws IOException {
    // Read the archived data stream via CanvasManager
    CanvasManager.LoadedProject project = canvasManager.loadCanvas(file);

    canvasPanel.clearCanvas();

    // Restore background image
    if (project.getBgImage() != null) {
      canvasPanel.setBackgroundImage(project.getBgImage());
    }

    // Restore note object hierarchy
    CanvasData loadedData = project.getCanvasData();
    if (loadedData != null && loadedData.getLabels() != null) {
      for (LabelData labelData : loadedData.getLabels()) {
        NoteObject note = NoteObject.fromData(labelData);
        canvasPanel.addLoadedNote(note);
      }
    }

    canvasPanel.revalidate();
    canvasPanel.repaint();
  }

  private void loadImageAsBackground(File file, Canvas canvasPanel) throws IOException {
    BufferedImage image = ImageIO.read(file);
    canvasPanel.setBackgroundImage(image);
  }
}