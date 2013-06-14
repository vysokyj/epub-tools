package name.vysoky.epub;

import org.junit.Test;

import java.io.File;
import java.util.Locale;

/**
 * Corrector test.
 * @author Jiri Vysoky
 */
public class CorrectorTest {

    @Test
    public void testCorrect() {
        try {
            File directory = new File("/home/dropbox/epub-project/src");
            Locale locale = new Locale("cs", "CZ");
            EpubTool epubTool = new EpubTool(directory);
            Corrector corrector = new Corrector(epubTool, locale);
            corrector.correct();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
