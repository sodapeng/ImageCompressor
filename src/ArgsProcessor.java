
/**
 * Created by Liangpeng on 6/2/17.
 */
public class ArgsProcessor {
  private String inputFile;
  private float ratio = 0;
  private String outputFile = "out.png";
  private boolean progressive;

  public ArgsProcessor(String[] args) {
    try {
      int index = 0;

      if (!args[index++].equals("-i")) {
        throw new IllegalArgumentException();
      }
      inputFile = args[index++];

      if (index < args.length && args[index].equals("-compress")) {
        index++;
        ratio = (float) (Integer.parseInt(args[index++]) / 100.0);
      }

      if (index < args.length && args[index].equals("-o")) {
        index++;
        outputFile = args[index++];
      }

      if (index < args.length) progressive = args[index].equals("-progressive");
    } catch (NumberFormatException e) {
      System.out.println("The ratio does not contain a parsable integer.");
      usage();
      throw e;
    } catch (IllegalArgumentException e) {
      usage();
      throw e;
    }
  }

  public String getInputFile() {
    return inputFile;
  }

  public float getRatio() {
    return ratio;
  }

  public String getOutputFile() {
    return outputFile;
  }

  public boolean isProgressive() {
    return progressive;
  }

  private void usage() {
    System.out.println("Usage:\njava ImageCompressor -i input-file [-compress ratio] [-o output-file] [-progressive]");
  }
}
