/* Assignment 4
 * First Name: John, Last Name: Goodacre
 * First Name: Liangpeng, Last Name: Zhuang
 * username: jgoodacre
 * username: sodapeng
 */

import java.io.IOException;
import java.util.List;


/**
 * This is a ImageCompressor class that takes the path of the input image file and options as
 * command-line arguments.
 * Usage:
 * java ImageCompressor -i input-file [-compress ratio] [-o output-file] [-progressive]
 */
public class ImageCompressor {

  /**
   * This is the entrance of the program.
   *
   * @param args input argument from command line
   * @throws IOException Thrown by ImageUtil
   */
  public static void main(String[] args) throws IOException {

    ArgsProcessor ap = new ArgsProcessor(args);

    //Check whether the command line argument is valid or not.
    //Return if it is not valid.
    if (!ap.isInputValid()) {
      return;
    }

    System.out.println("input file: " + ap.getInputFile());

    System.out.println("ratio: " + ap.getRatio());

    System.out.println("output file: " + ap.getOutputFile());

    System.out.println("is progressive: " + ap.isProgressive());

    //Read image from input file, and create an image object.
    int[][][] input = ImageUtil.readImage(ap.getInputFile());
    int imageHeight = ImageUtil.getHeight(ap.getInputFile());
    int imageWidth = ImageUtil.getWidth(ap.getInputFile());
    Image inputImage = new Image(input, imageWidth, imageHeight);

    //Compress the given image with given compression ratio, and generate a result image to given
    //output file.
    Image compressedImage = inputImage.compress(ap.getRatio());
    ImageUtil.writeImage(compressedImage.toArray(), imageWidth, imageHeight, ap.getOutputFile());

    //Generate a progressive compressed image if progressive command is passed in.
    //Create several files that show progressive levels of compression
    if (ap.isProgressive()) {
      List<Image> result = inputImage.progressiveCompress();
      for (int i = 0; i < result.size(); i++) {
        ImageUtil.writeImage(result.get(i).toArray(), imageWidth, imageHeight,
                ap.getInputFile().replaceAll(".png", "")
                        + (result.size() - i - 1) + ".png");
      }
    }
  }
}
