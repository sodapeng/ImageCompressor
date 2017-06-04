/* Assignment 3
 * First Name: John, Last Name: Goodacre
 * First Name: Liangpeng, Last Name: Zhuang
 * username: jgoodacre
 * username: sodapeng
 */

// test Seattle image file
// from https://www.tripadvisor.com/LocationPhotos-g60878-Seattle_Washington.html

// test Boston image file from
// http://wheatoncollege.edu/about/our-location/boston/


import java.io.IOException;
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

  public List<Image> progressiveCompress() {
    double[][][] squareimage = setSquare(image);
    List<Image> result = new ArrayList<Image>();
    int len = squareimage.length;
    progressiveHelper(squareimage, len, result);
    return result;

  }

  private void progressiveHelper(double[][][] squareImage, int num, List<Image> result) {
    if (num == 0) return;
    int len = squareImage.length;

    transform(squareImage);

    for (int i = 0; i <= len-1; i++) {
      for (int j = 0; j <= len-1; j++) {
        for (int p = 0; p < 3; p++) {
          if (i == 0 && j == 0) continue;
          else if (i < num && j < num) continue;
          else {
            squareImage[i][j][p] = 0;
          }
        }
      }
    }

    inverseTransform(squareImage);

    int[][][] backori = squareBack(doubleTermToInt(squareImage));
    result.add(new Image(backori, width, height));


    progressiveHelper(squareImage, num / 2, result);
  }

  public Image compress(float compressionRatio) {
    double[][][] squareImage = setSquare(image);
    int len = squareImage.length;
    int countzero = 0;
    double[] numberarray = new double[len * len * 3];
    int m = 0;

    transform(squareImage);

    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        for (int p = 0; p < 3; p++) {
          if (equalsZero(squareImage[i][j][p])) countzero++;
          numberarray[m++] = Math.abs(squareImage[i][j][p]);
        }
      }
    }

    Arrays.sort(numberarray);

    int nonemptybefore = (len * len * 3 - 3) - countzero;
    int emptyfinal = (int) ((len * len * 3 - 3) - nonemptybefore * (1 - compressionRatio));

    double threshold = numberarray[emptyfinal];

    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        for (int p = 0; p < 3; p++) {
          if(i == 0 && j == 0) continue;
          if (! greater(Math.abs(squareImage[i][j][p]), threshold))
            squareImage[i][j][p] = 0;
        }
      }
    }

    inverseTransform(squareImage);

    int[][][] backori = squareBack(doubleTermToInt(squareImage));

    return new Image(backori, width, height);
  }

  private void transform(double[][][] squareImage) {
    int h = squareImage.length;
    int w = squareImage[0].length;
    if (h != w) throw new RuntimeException("Image is not a square");
    int len = h;
    //start with row
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] rowdata = getRow(squareImage, i, p);
        transform(rowdata, len);
        for (int j = 0; j < len; j++) {
          squareImage[i][j][p] = rowdata[j];
        }
      }
    }
    //transform column
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] coldata = getCol(squareImage, i, p);
        transform(coldata, len);
        for (int j = 0; j < len; j++) {
          squareImage[j][i][p] = coldata[j];
        }
      }
    }
  }

  private void inverseTransform(double[][][] squareImage) {
    int h = squareImage.length;
    int w = squareImage[0].length;
    if (h != w) throw new RuntimeException("Image is not a square");
    int len = h;
    //transform column
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] coldata = getCol(squareImage, i, p);
        inverseTransform(coldata, 2);
        for (int j = 0; j < len; j++) {
          squareImage[j][i][p] = coldata[j];
        }
      }
    }
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] rowdata = getRow(squareImage, i, p);
        inverseTransform(rowdata, 2);
        for (int j = 0; j < len; j++) {
          squareImage[i][j][p] = rowdata[j];
        }
      }
    }
  }

  private static int[][][] doubleTermToInt(double[][][] image) {
    int h = image.length;
    int w = image[0].length;
    int[][][] result = new int[h][w][3];
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        for (int p = 0; p < 3; p++) {
          result[i][j][p] = (int) image[i][j][p];
        }
      }
    }
    return result;
  }

  private static void inverseTransform(double[] array, int start) {
    double[] base = new double[start];
    int m = 0;
    for (int i = 0; i < start / 2; i++) {
      double base1 = (array[i] + array[i + start / 2]) / Math.sqrt(2);
      double detail1 = (array[i] - array[i + start / 2]) / Math.sqrt(2);
      base[m++] = base1;
      base[m++] = detail1;
    }
    for (int i = 0; i < start; i++) {
      array[i] = base[i];
    }
    if (start == array.length) return;
    inverseTransform(array, start * 2);
  }

  private static void transform(double[] array, int end) {
    if (end == 1) return;
    double[] base = new double[end / 2];
    double[] detail = new double[end / 2];
    int m = 0;
    int n = 0;
    for (int i = 0; i < end; i += 2) {
      double base1 =  (array[i] + array[i + 1]) / Math.sqrt(2);
      double detail1 = (array[i] - array[i + 1]) / Math.sqrt(2);
      base[m++] = base1;
      detail[n++] = detail1;
    }
    for (int i = 0; i < end; i++) {
      if (i < end / 2) array[i] = base[i];
      else array[i] = detail[i - end / 2];
    }
    transform(array, end / 2);
  }

  private static double[] getRow(double[][][] array, int row, int color) {
    int len = array[0].length;
    double[] rowdata = new double[len];
    for (int i = 0; i < len; i++) {
      rowdata[i] = array[row][i][color];
    }
    return rowdata;
  }

  private static double[] getCol(double[][][] array, int col, int color) {
    int len = array.length;
    double[] coldata = new double[len];
    for (int i = 0; i < len; i++) {
      coldata[i] = array[i][col][color];
    }
    return coldata;
  }

  private static boolean isPowerTwo(int num) {
    return (num > 0) && (num & num - 1) == 0;
  }

  private double[][][] setSquare(int[][][] ori) {
    int len;
    int max = Math.max(ori.length, ori[0].length);
    if (isPowerTwo(max)) {
      len = max;
    }
    else {
      len = Integer.highestOneBit(max) * 2;
    }
    double[][][] newimagearray = new double[len][len][3];
    for (int i = 0; i < ori.length; i++) {
      for (int j = 0; j < ori[0].length; j++) {
        for (int p = 0; p < 3; p++)
          newimagearray[i][j][p] = (double) ori[i][j][p];
      }
    }
    return newimagearray;
  }

  private int[][][] squareBack(int[][][] square) {
    int[][][] ori = new int[height][width][3];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        for (int p = 0; p < 3; p++) {
          ori[i][j][p] = square[i][j][p];
        }
      }
    }
    return ori;
  }

  private static boolean equalsZero(double num) {
    double epsilon = 0.0000001;
    return Math.abs(num) < epsilon;
  }

  private static boolean greater(double num1, double num2) {
    double epsilon = 0.0000001;
    return num1 - num2 > epsilon;
  }

}

