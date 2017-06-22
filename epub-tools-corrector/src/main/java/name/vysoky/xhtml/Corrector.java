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

package name.vysoky.xhtml;

import name.vysoky.re.Replacer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Locale;

/**
 * Read XHTML file character by character and extract string between XML tags
 * than invoke string replacer to extracted text.
 */
@SuppressWarnings("unused")
public class Corrector {

    private Replacer stringCorrector;
    private boolean correctState = false;
    private StringBuilder builder = new StringBuilder();

    /**
     * Extract and invoke string replacer on re parts of the text part of the book.
     * @param locale locale used in corrections
     */
    public Corrector(Locale locale) {
        this.stringCorrector = new Replacer(locale);
    }

    public void correct(File xhtmlFile) throws IOException {
        File tempFile = new File(xhtmlFile.getAbsolutePath() + ".tmp");
        FileUtils.moveFile(xhtmlFile, tempFile);
        process(tempFile, xhtmlFile, "UTF-8");
        if (!tempFile.delete())
            throw new IOException("Unable to delete temp file '" + tempFile.getAbsolutePath() + "'!");

    }

    //TODO: Fix to print all differences
    private void printDifferences(String original, String revised) {
        int index = StringUtils.indexOfDifference(original, revised);
        if (index == -1) return;
        String difference = StringUtils.difference(original, revised);
        String before = original.substring(index);
        if (Replacer.isVerbose()) System.out.println("'" + before + "' -> '" + difference +"'");
    }

    private char[] correctAndReturnCharArray() {
        String original = builder.toString();
        String revised = stringCorrector.correct(original);
        printDifferences(original, revised);
        return revised.toCharArray();
    }

    private char[] processChar(char c) {
        char[] a = new char[0];
        if (c == '<') {
            // change state, add char to array and add buffer to array
            char[] b = correctAndReturnCharArray();
            builder = new StringBuilder();
            correctState = false;
            a = new char[1 + b.length];
            System.arraycopy(b, 0, a, 0, b.length);
            a[a.length - 1] = c;
        } else if (c == '>') {
            // change state and add char to array
            correctState = true;
            a = new char[1];
            a[0] = c;
        } else if (correctState) {
            // add char to buffer only
            builder.append(c);
        } else {
            // add char to array
            a = new char[1];
            a[0] = c;
        }
        return a;
    }

    private void process(File inputFile, File outputFile, String codePage) {
        Reader reader = null;
        Writer writer = null;
        try {
            reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(inputFile)), codePage);
            writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(outputFile)), codePage);
            char c;
            int b = reader.read();
            while(b != -1){
                c = (char) b;
                writer.write(processChar(c));
                b = reader.read();
            }
            if (builder.length() > 0) writer.write(builder.toString().toCharArray());
        } catch (IOException e) {
            System.err.println("Unable write streams!");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
            } catch (IOException e){
                System.err.println("Unable to close streams!");
                e.printStackTrace();
            }
        }
    }
}
