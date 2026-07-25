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
    System.out.println("Saving project...");
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Save Canvas Project");
    chooser.setFileFilter(new FileNameExtensionFilter("Canvas Project (*.json)", "json"));

    int result = chooser.showSaveDialog(canvas);
    if (result == JFileChooser.APPROVE_OPTION) {
      File targetFile = chooser.getSelectedFile();

      if (!targetFile.getName().toLowerCase().endsWith(".json")) {
        targetFile = new File(targetFile.getAbsolutePath() + ".json");
      }

      try {
        CanvasData data = new CanvasData();

        // 1. Convert Canvas Background Image to Base64
        BufferedImage bg = canvas.getBackgroundImage();
        if (bg != null) {
          data.setBase64ImageData(ImageUtils.toBase64(bg, "png"));
        }

        // 2. Convert Note Objects to Data DTOs
        List<LabelData> labelDataList = new ArrayList<>();
        for (NoteObject note : canvas.getNoteObjects()) {
          labelDataList.add(note.toData());
        }
        data.setLabels(labelDataList);

        canvasManager.saveCanvas(data, targetFile);
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

    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Canvas Project (*.json)", "json"));
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));

    int result = fileChooser.showOpenDialog(canvasPanel);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String fileName = selectedFile.getName().toLowerCase();

      try {
        if (fileName.endsWith(".json")) {
          loadCustomProject(selectedFile, canvasPanel);
        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
          loadImageAsBackground(selectedFile, canvasPanel);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(canvasPanel, "Failed to open file.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void loadCustomProject(File file, Canvas canvasPanel) throws IOException {
    CanvasData loadedData = canvasManager.loadCanvas(file);

    canvasPanel.clearCanvas();

    // Restore embedded background image
    if (loadedData.getBase64ImageData() != null && !loadedData.getBase64ImageData().isEmpty()) {
      BufferedImage bgImage = ImageUtils.fromBase64(loadedData.getBase64ImageData());
      canvasPanel.setBackgroundImage(bgImage);
    }

    // Restore note hierarchy
    if (loadedData.getLabels() != null) {
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