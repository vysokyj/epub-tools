/*
 * ePUB Corrector - https://github.com/vysokyj/epub-corrector/
 *
 * Copyright (C) 2012 Jiri Vysoky
 *
 * ePUB Corrector is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * ePUB Corrector is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cobertura; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package name.vysoky.epub;

import name.vysoky.xhtml.CorrectorFactory;
import name.vysoky.xhtml.TidyFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ePUB Replacer command line job and application entry point.
 */
public class Command {

    private static PrintStream out = System.out;
    private String[] args;
    private Option[] options = Option.values();
    private Map<Option,String> activeOptions; // option parameters
    private List<String> paths;


    public static void main(String[] args) {
        new Command(args).execute();
    }

    /**
     * Constructor for command line mode.
     * @param args command line arguments
     */
    public Command(String[] args) {
        this.args = args;
    }

    public void execute() {
        printCopyright();
        try {
            parseArguments(args);
        } catch (Exception e) {
            out.println("Wrong arguments!");
            out.println(e.getMessage());
            printHelp();
            System.exit(-1);
        }
        for (String path : paths) {
            try {
                execute(path);
                out.println("Successfully processed file: \"" + path + "\"");
            } catch (IOException e) {
                out.println("Error when processing file: \"" + path + "\"");
            }
        }
    }

    public static void printHeader(String string) {
        out.println();
        out.println("------------------------------------------------------------------------");
        out.println(string);
        out.println("------------------------------------------------------------------------");
    }

    private void parseArguments(String[] args) throws ParseException, FileNotFoundException {
        if (args.length == 0) throw new ParseException("No arguments!", 0);
        activeOptions = new HashMap<Option, String>(args.length);
        paths = new ArrayList<String>(args.length);
        for (int i = 0; i < args.length; i++) {
            String currentArgument = args[i];
            String nextArgument = (i < (args.length - 1)) ? args[i + 1] : null;
            int count = parseArgument(i, currentArgument, nextArgument);
            if (count == 2) i++;
        }
        if (paths.isEmpty()) throw new ParseException("Missing path argument!", 0);
    }

    private int parseArgument(int position, String currentArgument, String nextArgument)
            throws ParseException, FileNotFoundException {
        // process long argument
        if (currentArgument.startsWith("--"))
            return parserArgumentAsLongSwitch(position, currentArgument, nextArgument);
        // process short argument
        if (currentArgument.startsWith("-"))
            return parserArgumentAsShortSwitch(position, currentArgument, nextArgument);
        // process path
        return parserArgumentAsPath(position, currentArgument);
    }

    private int parserArgumentAsPath(int position, String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.isFile())
            throw new FileNotFoundException("Given argument '" +  path +
                    "'at position " + position + "is not a file path!");
        paths.add(path);
        return 1;
    }

    private int parserArgumentAsLongSwitch(int position, String currentArgument, String nextArgument)
            throws ParseException {
        String longSwitch = currentArgument.substring(2);
        Option option = getOption(longSwitch);
        return addOption(position, option, nextArgument);
    }

    private int parserArgumentAsShortSwitch(int position, String shortSwitch, String nextArgument)
            throws ParseException {
        char[] chars = shortSwitch.substring(1).toCharArray();
        int count = 1;
        for (char c : chars) {
            Option option = getOption(c);
            int currentCount = addOption(position, option, nextArgument);
            if (currentCount == 2) count = 2;
        }
        if (chars.length > 1 && count == 2) throw new ParseException("Do not group parametrised argument!", position);
        return count;
    }

    private int addOption(int argumentPosition, Option option, String nextArgument)
            throws ParseException {
        // check errors
        if (option == null)
            throw new ParseException("Unknown option!", argumentPosition);
        if (option.isParametrised() && !isParameter(nextArgument))
            throw new ParseException("Missing switch parameter!", argumentPosition + 1);
        if (option.isParametrised()) {
            activeOptions.put(option, nextArgument);
            return 2;
        } else {
            activeOptions.put(option, null);
            return 1;
        }
    }

    private Option getOption(String longSwitch) {
        for (Option o : options) if (o.getLongSwitch().equals(longSwitch)) return o;
        return null;
    }
    private Option getOption(char shortSwitch) {
        for (Option o : options) if (o.getShortSwitch().equals(shortSwitch)) return o;
        return null;
    }

    private boolean isParameter(String arg) {
        return ((arg != null) && !arg.startsWith("-"));
    }

    public void execute(String path) throws IOException {
        Book book = null;
        if (activeOptions.containsKey(Option.HELP)) {
            printHelp();
            System.exit(0);
        } else {
            book = new Book(path);
            book.setCorrector(CorrectorFactory.getDefaultCorrector());
            book.setTidy(TidyFactory.getDefaultTidy());
        }
        if (activeOptions.containsKey(Option.EXTRACT)) book.extract();
        if (activeOptions.containsKey(Option.CORRECT)) book.correct();
        if (activeOptions.containsKey(Option.TIDY)) book.tidy();
        // remove style first
        if (activeOptions.containsKey(Option.REMOVE_CLASS)) book.removeCSSClass(activeOptions.get(Option.REMOVE_CLASS));
        // than write out styles
        if (activeOptions.containsKey(Option.LIST_STYLES)) book.writeOutStyleClasses();
        if (activeOptions.containsKey(Option.COMPRESS)) book.compress();
        if (activeOptions.containsKey(Option.CHECK)) book.check();
    }

    private void printCopyright() {
        out.println("************************************************************************");
        out.println("* ePUB Corrector                                                       *");
        out.println("* Copyright (C) 2012 Jiri Vysoky                                       *");
        out.println("* Licensed under the GNU General Public License.                       *");
        out.println("************************************************************************");
    }

    private  void printHelp() {
        out.println();
        out.println("ussage:\tepub-corrector options file");
        out.println("options:");
        for (Option option : options) out.println(option);
    }
}
