/* Assignment 4
 * First Name: John, Last Name: Goodacre
 * First Name: Liangpeng, Last Name: Zhuang
 * username: jgoodacre
 * username: sodapeng
 */

/**
 * This is a utility class for compress a image.
 * Including transform and inverse transform a 3D image array, and helper functions for that.
 */

public class CompressUtil {

  /**
   * Change a given 3D image array to a square 3D image array by creating a new 3D square array, and
   * copy the original array data to new one.
   * @param ori the original (possibly rectangle) 3D image array
   * @return the square 3D image array
   */
  public static double[][][] setSquare(int[][][] ori) {

    //Find and increase the width and height to the next power of 2 and choose the greater one.
    int len;
    int max = Math.max(ori.length, ori[0].length);
    if (isPowerTwo(max)) {
      len = max;
    }
    else {
      len = Integer.highestOneBit(max) * 2;
    }

    //Pad extra 0 to the end of square image array.
    double[][][] newimagearray = new double[len][len][3];
    for (int i = 0; i < ori.length; i++) {
      for (int j = 0; j < ori[0].length; j++) {
        for (int p = 0; p < 3; p++)
          //Cast the original integer to double for later calculation.
          newimagearray[i][j][p] = (double) ori[i][j][p];
      }
    }
    return newimagearray;
  }

  /**
   * Convert the square 3D image array back to its original size.
   * @param square the square 3D image array
   * @return a 3D image array with its original width and height
   */
  public static int[][][] squareBack(int[][][] square, int width, int height) {
    int[][][] ori = new int[height][width][3];

    //Discard the padded rows and columns to obtain an image of the same size as the original image.
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        for (int p = 0; p < 3; p++) {
          ori[i][j][p] = square[i][j][p];
        }
      }
    }
    return ori;
  }

  /**
   * Transform each row, and each column of a given 3D image array, and mutate the given array to
   * transformed result.
   * @param squareImage the square 3D array of this image
   */
  public static void transform(double[][][] squareImage) {
    int len = squareImage.length;
    //transform row.
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] rowdata = getRow(squareImage, i, p);
        transform(rowdata);
        for (int j = 0; j < len; j++) {
          squareImage[i][j][p] = rowdata[j];
        }
      }
    }
    //transform column.
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] coldata = getCol(squareImage, i, p);
        transform(coldata);
        for (int j = 0; j < len; j++) {
          squareImage[j][i][p] = coldata[j];
        }
      }
    }
  }

  /**
   * Helper function for Transform a 3D image array.
   * Transform a given 1D array.
   * @param array a 1D array (each column and row of a 3D image array will be passed in)
   */

  private static void transform(double[] array) {
    int len = array.length;
    while (len > 1) {
      double[] base = new double[len/2];
      double[] detail = new double[len/2];
      int m = 0;
      int n = 0;
      for (int i = 0; i < len; i += 2) {
        double base1 = (double) (array[i] + array[i+1]) / Math.sqrt(2);
        double detail1 = (double) (array[i] - array[i+1]) / Math.sqrt(2);
        base[m++] = base1;
        detail[n++] = detail1;
      }
      for (int i = 0; i < len; i++) {
        if (i < len/2) array[i] = base[i];
        else array[i] = detail[i - len/2];
      }
      len = len / 2;
    }

  }

  /**
   * Inverse transform each row, and each column of a given 3D image array, and mutate the
   * given array to inverse-transformed result.
   * @param squareImage the square 3D array of this image
   */
  public static void inverseTransform(double[][][] squareImage) {
    int len = squareImage.length;
    //inverse transform column.
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] coldata = getCol(squareImage, i, p);
        inverseTransform(coldata);
        for (int j = 0; j < len; j++) {
          squareImage[j][i][p] = coldata[j];
        }
      }
    }
    //inverse transform row.
    for (int i = 0; i < len; i++) {
      for (int p = 0; p < 3; p++) {
        double[] rowdata = getRow(squareImage, i, p);
        inverseTransform(rowdata);
        for (int j = 0; j < len; j++) {
          squareImage[i][j][p] = rowdata[j];
        }
      }
    }
  }

  /**
   * Helper function for Inverse Transform a 3D image array.
   * Inverse-transform a given 1D array.
   * @param array a 1D array (each column and row of a 3D image array will be passed in)
   */
  private static void inverseTransform(double[] array) {
    int start = 2;
    while (start <= array.length) {
      double[] base = new double[start];
      int m = 0;
      for (int i = 0; i < start/2; i++) {
        double base1 = (array[i] + array[i+start/2]) / Math.sqrt(2);
        double detail1 = (array[i] - array[i+start/2]) / Math.sqrt(2);
        base[m++] = base1;
        base[m++] = detail1;
      }
      for (int i = 0; i < start; i++) {
        array[i] = base[i];
      }
      start = start * 2;
    }
  }

  /**
   * Convert the square 3D image double array to 3D image integer array for finial image output
   *
   * @param image the square 3D double array of this image
   * @return the square 3D integer array of this image
   */
  public static int[][][] doubleTermToInt(double[][][] image) {
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

  /**
   * Get data from each row of this 3D image array.
   * @param array 3D image array
   * @param row the row of which the data need to be extracted
   * @param color the channel of which the data need to be extracted
   * @return the data in given row and channel
   */
  private static double[] getRow(double[][][] array, int row, int color) {
    int len = array[0].length;
    double[] rowdata = new double[len];
    for (int i = 0; i < len; i++) {
      rowdata[i] = array[row][i][color];
    }
    return rowdata;
  }

  /**
   * Get data from each column of this 3D image array.
   * @param array 3D image array
   * @param col the col of which the data need to be extracted
   * @param color the channel of which the data need to be extracted
   * @return the data in given col and channel
   */
  private static double[] getCol(double[][][] array, int col, int color) {
    int len = array.length;
    double[] coldata = new double[len];
    for (int i = 0; i < len; i++) {
      coldata[i] = array[i][col][color];
    }
    return coldata;
  }

  /**
   * Check whether a number is power of 2 using bit manipulation.
   * Return true if the number is power of 2, and false if it is not.
   * @param num the given number
   * @return true if the number is power of 2, otherwise false.
   */
  private static boolean isPowerTwo(int num) {
    return (num > 0) && (num & num - 1) == 0;
  }
}
