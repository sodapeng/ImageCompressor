import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Has utility methods to read an image from file and write to a file.
 */
public class ImageUtil {

  /**
   * Reads the given image file.
   */
  public static int[][][] readImage(String filename) throws IOException {
    BufferedImage input;

    input = ImageIO.read(new FileInputStream(filename));

    int[][][] result = new int[input.getHeight()][input.getWidth()][3];

    for (int i = 0; i < input.getHeight(); i++) {
      for (int j = 0; j < input.getWidth(); j++) {
        int color = input.getRGB(j, i);
        Color c = new Color(color);
        result[i][j][0] = c.getRed();
        result[i][j][1] = c.getGreen();
        result[i][j][2] = c.getBlue();
      }
    }
    return result;
  }

  /**
   * Gets the width of the input file.
   *
   * @return the image width
   */
  public static int getWidth(String filename) throws IOException {
    BufferedImage input;

    input = ImageIO.read(new FileInputStream(filename));

    return input.getWidth();
  }

  /**
   * Gets the height of the input file.
   *
   * @return the image height
   */
  public static int getHeight(String filename) throws IOException {
    BufferedImage input;

    input = ImageIO.read(new FileInputStream(filename));

    return input.getHeight();
  }


  /**
   * Writes a new image file.
   */
  public static void writeImage(int[][][] rgb, int width, int height, String
          filename)
          throws IOException {

    BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = rgb[i][j][0];
        int g = rgb[i][j][1];
        int b = rgb[i][j][2];

        // color is stored in 1 integer, with the 4 bytes storing ARGB in that order.
        // Each of r,g,b are stored in 8 bits (hence between 0 and 255).
        // So we put them all in one integer by using bit-shifting << as below.
        int color = (r << 16) + (g << 8) + b;
        output.setRGB(j, i, color);
      }
    }
    String extension = filename.substring(filename.indexOf(".") + 1);
    ImageIO.write(output, extension, new FileOutputStream(filename));
  }

  public static void main(String[] args) throws IOException {
    ImageUtil test = new ImageUtil();
    int[][][] result = test.readImage("test image.jpg");
  }
}
