package name.vysoky.epub.cli;

import name.vysoky.epub.EpubConverter;
import org.apache.commons.cli.*;

import java.io.IOException;

/**
 * Command line tool.
 * @author Jiri Vysoky
 */
public class Tool {

    public static void main(String[] args) {
        CommandLineParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("t", "text", false, "extractPlainText ePUB content as plain text"));
        options.addOption(new Option("h", "help", false, "print this message"));
        options.addOption(new Option("s", "simplify", false, "simplify text by converting special chars"));
        HelpFormatter formatter = new HelpFormatter();
        try {
            if (args.length == 0) throw new ParseException("Missing options!");
            CommandLine line = parser.parse(options, args);
            if (line.getArgList().size() == 0) throw new ParseException("Missing arguments!");
            if (line.hasOption("text")) {
                String input = line.getArgList().get(0).toString();
                String output = (line.getArgList().size() == 2) ? line.getArgList().get(1).toString() : null;
                boolean simplify = line.hasOption("simplify");
                EpubConverter.convertToPlainText(input, output, simplify);
            }
        } catch (ParseException e) {
            System.err.println("Command line parsing failed. Reason: " + e.getMessage());
            formatter.printHelp("epubtool", options );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
