import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

public class CanvasManager {
  private final ObjectMapper mapper;

  public CanvasManager() {
    this.mapper = new ObjectMapper();
    // Formats JSON
    this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

    this.mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
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

        InputStream nonClosingStream = new NonClosingInputStream(zis);

        if (entryName.equals("data.json")) {
          canvasData = mapper.readValue(nonClosingStream, CanvasData.class);
        } else if (entryName.equals("background.png")) {
          bgImage = ImageIO.read(nonClosingStream);
        }
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

  private static class NonClosingInputStream extends InputStream {
    private final InputStream delegate;

    public NonClosingInputStream(InputStream delegate) {
      this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
      return delegate.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      return delegate.read(b, off, len);
    }

    @Override
    public int available() throws IOException {
      return delegate.available();
    }

    @Override
    public long skip(long n) throws IOException {
      return delegate.skip(n);
    }

    @Override
    public void close() {
      // prevents Jackson / ImageIO from closing ZipInputStream
    }
  }
}