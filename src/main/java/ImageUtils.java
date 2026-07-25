import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class ImageUtils {

  // Converts a BufferedImage to a Base64 string for JSON embedding
  public static String toBase64(BufferedImage image, String formatName) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(image, formatName != null ? formatName : "png", baos);
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  // Converts Base64 string back to a BufferedImage
  public static BufferedImage fromBase64(String base64Str) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(base64Str);
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    return ImageIO.read(bais);
  }
}