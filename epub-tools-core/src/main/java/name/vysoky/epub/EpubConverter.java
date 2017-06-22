package name.vysoky.epub;

import name.vysoky.xhtml.XhtmlTool;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Fast end simple EPUB format converter.
 * Converts to TXT for now.
 *
 * @author Jiri Vysoky
 */
public class EpubConverter {

    /**
     * Convert EPUB to plain text file.
     * @param input path to EPUB file
     * @param output path to TXT file - optional, if null printed to std out
     * @param simplify if true than text format is unified to simple form - e.g. removed smart quotes, dashes...
     * @throws IOException common IOException
     */
    public static void convertToPlainText(String input, String output, boolean simplify) throws IOException {
        File epubFile = new File(input);
        File textFile = (output == null) ? null : new File(output);
        EpubReader reader = new EpubReader();
        StringBuilder stringBuilder = new StringBuilder();
        Book book = reader.readEpub(new FileInputStream(epubFile));
        Spine spine = book.getSpine();
        String string; // extracted text
        for (SpineReference spineReference : spine.getSpineReferences())  {
            Resource resource = spineReference.getResource();
            try {
                XhtmlTool.extractPlainText(resource.getInputStream(), stringBuilder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        string = stringBuilder.toString();
        if (simplify) {
            string = SmartQuoter.convertToDefaultQuotes(string);
            string = string.replaceAll("\u2013", "-"); // replace dash with minus
            string = string.replaceAll("\u00A0", " ");  // replace non breaking space with space
        }
        if (textFile == null) System.out.println(string);
        else FileUtils.writeStringToFile(textFile, string);
    }
}
