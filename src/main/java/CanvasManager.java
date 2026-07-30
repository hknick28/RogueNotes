import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

public class CanvasManager {
  private final ObjectMapper mapper;

  public CanvasManager() {
    this.mapper = new ObjectMapper();
    // Formats JSON nicely so it's human-readable
    this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  // Save Canvas Data to zip file
  public void saveCanvas(CanvasData canvasData, BufferedImage bgImage, File file) throws IOException {
    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {

      // Pack JSON metadata into 'data.json'
      ZipEntry jsonEntry = new ZipEntry("data.json");
      zos.putNextEntry(jsonEntry);
      byte[] jsonBytes = mapper.writeValueAsBytes(canvasData);
      zos.write(jsonBytes);
      zos.closeEntry();

      // Pack raw background image into 'background.png'
      if (bgImage != null) {
        ZipEntry imgEntry = new ZipEntry("background.png");
        zos.putNextEntry(imgEntry);
        ImageIO.write(bgImage, "png", zos);
        zos.closeEntry();
      }
    }
  }

  // Load Canvas Data from zip file
  public LoadedProject loadCanvas(File file) throws IOException {
    CanvasData canvasData = null;
    BufferedImage bgImage = null;

    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        String entryName = entry.getName();

        if (entryName.equals("data.json")) {
          canvasData = mapper.readValue(zis, CanvasData.class);
        } else if (entryName.equals("background.png")) {
          bgImage = ImageIO.read(zis);
        }
        zis.closeEntry();
      }
    }

    return new LoadedProject(canvasData, bgImage);
  }

  public static class LoadedProject {
    private final CanvasData canvasData;
    private final BufferedImage bgImage;

    public LoadedProject(CanvasData canvasData, BufferedImage bgImage) {
      this.canvasData = canvasData;
      this.bgImage = bgImage;
    }

    public CanvasData getCanvasData() { return canvasData; }
    public BufferedImage getBgImage() { return bgImage; }
  }
}