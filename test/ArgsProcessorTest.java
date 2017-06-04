
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Created by ningli on 6/2/17.
 */
public class ArgsProcessorTest {
    private float epsilon = (float) 0.00001;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }

    @Test
    public void testGoodInput() throws Exception {
        String[] args = {"-i", "/home/input", "-compress", "75", "-o", "/home/output", "-progressive"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertEquals(ap.getInputFile(), "/home/input");
        assertEquals(ap.getRatio(), (float) 0.75, epsilon);
        assertEquals(ap.getOutputFile(), "/home/output");
        assertEquals(ap.isProgressive(), true);
    }

    @Test
    public void testGoodInputWithoutOptions() throws Exception {
        String[] args = {"-i", "/home/input"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertEquals(ap.getInputFile(), "/home/input");
        assertEquals(ap.getRatio(), (float) 0, epsilon);
        assertEquals(ap.getOutputFile(), "out.png");
        assertEquals(ap.isProgressive(), false);
    }

    @Test
    public void testGoodInputWithRatio() throws Exception {
        String[] args = {"-i", "/home/input",  "-compress", "75"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertEquals(ap.getInputFile(), "/home/input");
        assertEquals(ap.getRatio(), (float) 0.75, epsilon);
        assertEquals(ap.getOutputFile(), "out.png");
        assertEquals(ap.isProgressive(), false);
    }

    @Test
    public void testGoodInputWithOutputFile() throws Exception {
        String[] args = {"-i", "/home/input",  "-o", "/home/output"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertEquals(ap.getInputFile(), "/home/input");
        assertEquals(ap.getRatio(), (float) 0, epsilon);
        assertEquals(ap.getOutputFile(), "/home/output");
        assertEquals(ap.isProgressive(), false);
    }

    @Test
    public void testGoodInputWithProgressive() throws Exception {
        String[] args = {"-i", "/home/input", "-progressive"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertEquals(ap.getInputFile(), "/home/input");
        assertEquals(ap.getRatio(), (float) 0, epsilon);
        assertEquals(ap.getOutputFile(), "out.png");
        assertEquals(ap.isProgressive(), true);
    }

    @Test
    public void testGoodInputWithRatioAndProgressive() throws Exception {
        String[] args = {"-i", "/home/input", "-compress", "75", "-progressive"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertEquals(ap.getInputFile(), "/home/input");
        assertEquals(ap.getRatio(), (float) 0.75, epsilon);
        assertEquals(ap.getOutputFile(), "out.png");
        assertEquals(ap.isProgressive(), true);
    }

    @Test
    public void testBadInputWithInvalidInputFile() throws Exception {
        String[] args = {"/home/input"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertEquals(ap.getInputFile(), null);
        assertEquals(ap.getRatio(), (float) 0, epsilon);
        assertEquals(ap.getOutputFile(), "out.png");
        assertEquals(ap.isProgressive(), false);
        assertTrue(outContent.toString()
                .contains("java ImageCompressor -i input-file [-compress ratio] [-o output-file] [-progressive]"));
    }

    @Test
    public void testBadInputWithInvalidRatio() throws Exception {
        String[] args = {"-i", "/home/input", "-compress", "hello"};
        ArgsProcessor ap = new ArgsProcessor(args);
        assertTrue(outContent.toString()
                .contains("The ratio does not contain a parsable integer"));
    }
}