/* Assignment 4
 * First Name: John, Last Name: Goodacre
 * First Name: Liangpeng, Last Name: Zhuang
 * username: jgoodacre
 * username: sodapeng
 */

// test Seattle image file
// from http://travel.nationalgeographic.com/travel/city-guides/seattle-photos-2/#/
// seattle-queen-anne_2487_600x450.jpg

// test Boston image file from
// http://wheatoncollege.edu/about/our-location/boston/


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a Image class represent an image using a 3D array of integers.
 */

public class Image {
  private final int[][][] image;
  private final int width;
  private final int height;

  /**
   * Construct an Image object with a 3D array, width and height.
   *
   * @param image  the 3D array representation of image
   * @param width  the width of the image
   * @param height the height of the image
   */

  public Image(int[][][] image, int width, int height) {
    this.image = image;
    this.width = width;
    this.height = height;

  }

  /**
   * Construct an Image object with only width and height, which creates a black image(r=g=b=0).
   *
   * @param width  the width of the image
   * @param height the height of the image
   */
  public Image(int width, int height) {
    this.width = width;
    this.height = height;
    this.image = new int[height][width][3];

  }

  /**
   * Returns the contents of this image as a 3D array.
   *
   * @return the 3D representation of this image
   */

  public int[][][] toArray() {
    return this.image;
  }


  /**
   * Gets the width of this image.
   *
   * @return the image width
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Gets the height of this image.
   *
   * @return the image height
   */
  public int getHeight() {
    return this.height;
  }


  /**
   * Filters the given pixel in the given channel using the given kernel.
   *
   * @param kernel  the kernal used to filter
   * @param row     the row of the pixel to be filtered
   * @param col     the column of the pixel to be filtered
   * @param channel the channel of the pixel to be filtered
   * @return the result of the filtered pixel
   */
  private float filter(float[][] kernel, int row, int col, int channel) {
    int kernellen = kernel.length;
    int kernelcenter = kernellen / 2;
    int rowdiff = row - kernelcenter;
    int coldiff = col - kernelcenter;
    int i = 0;
    int j = 0;
    float result = 0;
    for (i = Math.max(0, row - kernelcenter); i <= row + kernelcenter && i < height; i++) {
      for (j = Math.max(0, col - kernelcenter); j <= col + kernelcenter && j < width; j++) {
        result += image[i][j][channel] * kernel[i - rowdiff][j - coldiff];
      }
    }
    return result;
  }

  /**
   * Returns the result of blurring this image.
   *
   * @return a new Image object of the result of blurring this image.
   */
  public Image blur() {
    int[][][] blurarray = new int[height][width][3];
    float[][] kernerlblur = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        blurarray[i][j][0] = (int) filter(kernerlblur, i, j, 0);
        blurarray[i][j][1] = (int) filter(kernerlblur, i, j, 1);
        blurarray[i][j][2] = (int) filter(kernerlblur, i, j, 2);
      }
    }
    blurarray = normalize(blurarray);
    return new Image(blurarray, width, height);
  }

  /**
   * Normalizes the image by rescaling out of range color values.
   *
   * @return the new image array
   */

  private int[][][] normalize(int[][][] imagearray) {
    int h = imagearray.length;
    int w = imagearray[0].length;
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        for (int p = 0; p < 3; p++) {
          max = Math.max(max, imagearray[i][j][p]);
          min = Math.min(min, imagearray[i][j][p]);
        }
      }
    }
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        for (int p = 0; p < 3; p++) {
          imagearray[i][j][p] = (imagearray[i][j][p] - min) * 255 / (max - min);
        }
      }
    }
    return imagearray;
  }

  /**
   * Converts the image to greyscale.
   *
   * @return the new image array
   */
  private int[][][] greyScale(int[][][] imagearray) {
    int h = imagearray.length;
    int w = imagearray[0].length;
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int r = imagearray[i][j][0];
        int g = imagearray[i][j][1];
        int b = imagearray[i][j][2];
        double v = 0;

        v = ((Math.sqrt(r * r + g * g + b * b)) / 3);

        for (int k = 0; k < 3; ++k) {
          imagearray[i][j][k] = (int) v;
        }
      }
    }
    return normalize(imagearray);
  }

  /**
   * Returns the image as greyscale with edges highlighted.
   *
   * @return a new Image object of the result of edge-detecting
   */
  public Image edge() {
    int[][][] edgearray = new int[height][width][3];
    float[][] kx = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
    float[][] ky = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        for (int k = 0; k < 3; k++) {

          float gx = filter(kx, i, j, k);
          float gy = filter(ky, i, j, k);

          edgearray[i][j][k] = (int) Math.sqrt(gx * gx + gy * gy);
        }
      }
    }
    edgearray = greyScale(normalize(edgearray));

    return new Image(edgearray, width, height);
  }

  /**
   * Progressively compress an this image, and starting with the original image and ending with
   * the most compressed image.
   *
   * @return Return a list of compressed image, start with the original image and end with the most
   * compressed image
   */

  public List<Image> progressiveCompress() {
    double[][][] squareimage = CompressUtil.setSquare(image);
    List<Image> result = new ArrayList<Image>();
    int len = squareimage.length;
    progressiveHelper(squareimage, result);
    return result;

  }

  /**
   * Helper function for progressive compress. Set more detailed terms to 0 over time, util all the
   * details are set to 0.
   *
   * @param squareImage the square 3D array of this image
   * @param result      store a list of images that getting so far
   */
  private void progressiveHelper(double[][][] squareImage, List<Image> result) {
    int num = squareImage.length;
    while (num != 0) {
      int len = squareImage.length;

      //Transform the given square image.
      CompressUtil.transform(squareImage);

      //Set detail terms to 0.
      for (int i = 0; i <= len - 1; i++) {
        for (int j = 0; j <= len - 1; j++) {
          for (int p = 0; p < 3; p++) {
            if ((i == 0 && j == 0) || (i < num && j < num)) continue;
            else {
              squareImage[i][j][p] = 0;
            }
          }
        }
      }

      //Inverse transform the processed square image.
      CompressUtil.inverseTransform(squareImage);

      //Convert the compressed image 3D array to original height and width, and generate a new Image
      //object, and then add to list.
      int[][][] intimage = CompressUtil.doubleTermToInt(squareImage);
      int[][][] backori = CompressUtil.squareBack(intimage, width, height);
      result.add(new Image(backori, width, height));
      num = num / 2;
    }
  }

  /**
   * Compress this image with given compression ratio.
   * The compression ratio is expressed as a number between 0 and 1
   *
   * @param compressionRatio the compression ratio
   * @return a compressed image
   */
  public Image compress(float compressionRatio) {
    double[][][] squareImage = CompressUtil.setSquare(image);
    int len = squareImage.length;
    int countzero = 0;
    double[] numberarray = new double[len * len * 3];
    int m = 0;

    //Transform the given square image.
    CompressUtil.transform(squareImage);

    //count the number of 0 in original 3D image array, and store all the data in a 1D array.
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        for (int p = 0; p < 3; p++) {
          if (equalsZero(squareImage[i][j][p])) countzero++;
          numberarray[m++] = Math.abs(squareImage[i][j][p]);
        }
      }
    }
    //Sort the number array for threshold calculation.
    Arrays.sort(numberarray);

    //Calculate the threshold based on compression ratio.
    int nonemptybefore = (len * len * 3 - 3) - countzero;
    int emptyfinal = (int) ((len * len * 3 - 3) - nonemptybefore * (1 - compressionRatio));

    double threshold = numberarray[emptyfinal];

    //Set the details terms to 0 which absolute value if smaller than threshold.
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        for (int p = 0; p < 3; p++) {
          if (i == 0 && j == 0) continue;
          if (!greater(Math.abs(squareImage[i][j][p]), threshold))
            squareImage[i][j][p] = 0;
        }
      }
    }

    //Inverse transform the processed square image.
    CompressUtil.inverseTransform(squareImage);

    //Convert the compressed image 3D array to original height and width, and generate a new Image
    //object.
    int[][][] intimage = CompressUtil.doubleTermToInt(squareImage);
    int[][][] backori = CompressUtil.squareBack(intimage, width, height);

    return new Image(backori, width, height);
  }

  /**
   * Check whether a double is equal to 0 or not.
   *
   * @param num the given number
   * @return true if the double is equal to 0, otherwise false
   */
  private static boolean equalsZero(double num) {
    double epsilon = 0.0000001;
    return Math.abs(num) < epsilon;
  }

  /**
   * Check whether a double number is greater than another double number or not.
   *
   * @param num1 the first double number
   * @param num2 the second double number
   * @return true if the first double number is greater than the second one, other wise false.
   */
  private static boolean greater(double num1, double num2) {
    double epsilon = 0.0000001;
    return num1 - num2 > epsilon;
  }

}

