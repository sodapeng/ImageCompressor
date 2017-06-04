import java.io.IOException;
import java.util.List;

public class ImageCompressor {

  public static void main(String[] args) throws IOException {

    ArgsProcessor ap = new ArgsProcessor(args);

    System.out.println("input file: " + ap.getInputFile());

    System.out.println("ratio: " + ap.getRatio());

    System.out.println("output file: " + ap.getOutputFile());

    System.out.println("is progressive: " + ap.isProgressive());

    int[][][] input = ImageUtil.readImage(ap.getInputFile());
    int imageHeight = ImageUtil.getHeight(ap.getInputFile());
    int imageWidth = ImageUtil.getWidth(ap.getInputFile());
    Image inputImage = new Image(input, imageWidth, imageHeight);
    Image compressedImage = inputImage.compress(ap.getRatio());
    ImageUtil.writeImage(compressedImage.toArray(), imageWidth, imageHeight, ap.getOutputFile());

    if (ap.isProgressive()) {
      List<Image> result = inputImage.progressiveCompress();
      for (int i = 0; i < result.size(); i++) {
        ImageUtil.writeImage(result.get(i).toArray(), imageWidth, imageHeight,
                ap.getInputFile().replaceAll(".png", "") + (result.size() - i - 1) + ".png");
      }
    }
  }
}
