package name.vysoky.epub2txt;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * EPUB to TXT converter
 *
 * @author Jiri Vysoky
 */
public class Converter {

    private EpubReader reader = new EpubReader();
    private TextExtractor extractor = new TextExtractor();

    public static void main(String[] args) {
       if (args.length != 2) {
           System.out.println("No arguments exiting...");
           System.exit(1);
       }
       File epubFile = new File(args[0]);
       File txtFile = new File(args[1]);
        try {
            Converter converter = new Converter();
            converter.convert(epubFile, txtFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public void convert(File epubFile, File txtFile) throws IOException {
        Book book = reader.readEpub(new FileInputStream(epubFile));
        Spine spine = book.getSpine();
        for (SpineReference spineReference : spine.getSpineReferences())
            extractor.read(spineReference.getResource().getInputStream());
        extractor.write(new FileOutputStream(txtFile));
        extractor.clean();
    }

}
