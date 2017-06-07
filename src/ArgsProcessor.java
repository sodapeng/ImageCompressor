/* Assignment 4
 * First Name: John, Last Name: Goodacre
 * First Name: Liangpeng, Last Name: Zhuang
 * username: jgoodacre
 * username: sodapeng
 */

/**
 * This is the argument parser, the expected input is:
 * java ImageCompressor -i input-file [-compress ratio] [-o output-file] [-progressive]
 * The input-file is the name of the input file with extension.
 * The parts in [] are the options.
 * If -compress is specified, it must be followed by the ratio as a percentage.
 * The -o flag should be followed by the name of the output file with extension.
 * If the -o option is not given, the default name of the output file should be “out.png”.
 * The -progressive flag should create several files that show progressive levels of compression.
 */
public class ArgsProcessor {
  private String inputFile;
  private float ratio = 0;
  private String outputFile = "out.png";
  private boolean progressive;
  private boolean isInputValid;

  /**
   * Argument parser constructor.
   * Throw IllegalArgumentExcpetion when compression ratio is not integer or is not within the
   * range of 0-100.
   * @param args input arguments from command line
   * @throws IllegalArgumentException when arguments are illegal
   */
  public ArgsProcessor(String[] args) throws IllegalArgumentException {
    try {
      int index = 0;

      if (!args[index++].equals("-i")) {
        throw new IllegalArgumentException();
      }
      inputFile = args[index++];

      if (index < args.length && args[index].equals("-compress")) {
        index++;
        ratio = (float) (Integer.parseInt(args[index++]) / 100.0);
        if (ratio > 1 || ratio < 0) {
          throw new IllegalArgumentException("Compression ratio should be in range of 0 - 100");
        }
      }

      if (index < args.length && args[index].equals("-o")) {
        index++;
        outputFile = args[index++];
      }
      if (index < args.length) progressive = args[index].equals("-progressive");

      isInputValid = true;
    } catch (NumberFormatException e) {
      System.out.println("The ratio does not contain a parsable integer.");
      usage();
    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
      usage();
    }
  }

  /**
   * Get input file path
   * @return input file path
   */
  public String getInputFile() {
    return inputFile;
  }

  /**
   * Get ratio
   * @return ratio
   */
  public float getRatio() {
    return ratio;
  }

  /**
   * Get output file path
   * @return output file path
   */
  public String getOutputFile() {
    return outputFile;
  }

  /**
   * Is progressive flag set
   * @return is progressive flag set
   */
  public boolean isProgressive() {
    return progressive;
  }

  /**
   * Is input a valid command
   * @return is input a valid command
   */
  public boolean isInputValid() { return isInputValid; }

  /**
   * Print usage
   */
  private void usage() {
    System.out.println("Usage:\njava ImageCompressor -i input-file [-compress ratio] [-o output-file] [-progressive]");
  }
}
