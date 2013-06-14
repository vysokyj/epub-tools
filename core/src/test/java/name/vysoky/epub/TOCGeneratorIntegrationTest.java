package name.vysoky.epub;

import org.junit.Test;

import java.io.File;

/**
 * Table of content generator tests.
 *
 * @author Jiri Vysoky
 */
public class TOCGeneratorIntegrationTest {

    @Test
    public void generatorTest() throws Exception {
        String sampleDirectoryPath = System.getProperty("sampleDirectoryPath");
        File directory = new File(sampleDirectoryPath);
        EpubTool tool = new EpubTool(directory);
        TOCGenerator generator = new TOCGenerator(tool, 1, 6);
        generator.generate();
    }
}
