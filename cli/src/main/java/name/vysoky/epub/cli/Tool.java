package name.vysoky.epub.cli;

import name.vysoky.epub.TextExtractor;
import name.vysoky.epub.XhtmlProcessor;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Command line tool.
 * @author Jiri Vysoky
 */
public class Tool {

    public static void main(String[] args) {
        CommandLineParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("t", "text", false, "extract ePUB content as plain text"));
        options.addOption(new Option("h", "help", false, "print this message"));
        HelpFormatter formatter = new HelpFormatter();
        try {
            if (args.length == 0) throw new ParseException("Missing options!");
            CommandLine line = parser.parse(options, args);
            if (line.getArgList().size() == 0) throw new ParseException("Missing arguments!");
            if (line.hasOption("text")) {
                System.out.println("Converting ePUB to plain text...");
                File epubFile = new File(line.getArgList().get(0).toString());
                System.out.println("Input ePUB file: " + epubFile);
                EpubReader reader = new EpubReader();
                TextExtractor extractor = new TextExtractor();
                Book book = reader.readEpub(new FileInputStream(epubFile));
                Spine spine = book.getSpine();
                for (SpineReference spineReference : spine.getSpineReferences())  {
                    Resource resource = spineReference.getResource();
                    XhtmlProcessor processor = new XhtmlProcessor(extractor);
                    processor.process(IOUtils.toString(resource.getInputStream(), resource.getInputEncoding()));
                }
                if (line.getArgList().size() == 2) {
                    File textFile = new File(line.getArgList().get(1).toString());
                    FileUtils.writeStringToFile(textFile, extractor.getText());
                } else {
                    System.out.println(extractor.getText());
                }

            }
        } catch (ParseException e) {
            System.err.println("Command line parsing failed. Reason: " + e.getMessage());
            formatter.printHelp("epubtool", options );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
