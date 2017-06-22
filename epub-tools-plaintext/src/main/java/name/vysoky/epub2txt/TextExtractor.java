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

package name.vysoky.epub2txt;

import java.io.*;

/**
 * Read XHTML file character by character and extracts string between XML tags.
 */
@SuppressWarnings("unused")
public class TextExtractor {

    private boolean processChars = false;
    private boolean processEntity = false;
    private boolean flushEntity = false;
    private StringBuilder builder = new StringBuilder();
    private StringBuilder entity = new StringBuilder();

    private String inputCodePage = "UTF-8";
    private String outputCodePage = "UTF-8";

    /**
     * Extract and invoke string replacer on re parts of the text part of the book.
     */
    public TextExtractor() { }

    public TextExtractor(String inputCodePage, String outputCodePage) {
        this.inputCodePage = inputCodePage;
        this.outputCodePage = outputCodePage;
    }

    public void read(InputStream inputStream) throws IOException {
        read(inputStream, inputCodePage);
    }

    public void write(OutputStream outputStream) throws IOException {
        write(outputStream, outputCodePage);
    }

    public void clean() {
        builder = new StringBuilder();
    }


    private boolean processSwitch(char c) {
        switch (c) {
            case '>':
                processChars = true;
                return true;
            case '<':
                processChars = false;
                return true;
            case '&':
                processChars = false;
                processEntity = true;
                return true;
            case ';':
                if (processEntity) { // used as entity end
                    processChars = true;
                    processEntity = false;
                    flushEntity = true;
                    return true;
                } else { // used as regular character
                    return false;
                }
            default:
                return false;

        }
    }

    private void processChar(char c) {
        // actually on switching char do not continue
        if (processSwitch(c)) return;

        // flush buffers first
        if (flushEntity) {
            entity.append(';');
            try {
                builder.append(EntityMapper.toChar(entity.toString()));
            } catch (Exception e) {
                System.err.println("Unknown entity: \"" + entity.toString() + "\"");
            }
            entity = new StringBuilder();
            flushEntity = false;
        }

        if (processEntity) {
            if (entity.length() == 0) entity.append('&');
            entity.append(c);
        }

        if (processChars) {
            builder.append(c);
        }
    }

    private void read(InputStream inputStream, String codePage) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(new BufferedInputStream(inputStream), codePage);
            char c;
            int b = reader.read();
            while(b != -1){
                c = (char) b;
                processChar(c);
                b = reader.read();
            }
        } catch (IOException e) {
            System.err.println("Unable write streams!");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e){
                System.err.println("Unable to close streams!");
                e.printStackTrace();
            }
        }
    }

    private void write(OutputStream outputStream, String codePage) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new BufferedOutputStream(outputStream), codePage);
            //writer.write(builder.toString().toCharArray());
            for (int i = 0; i < builder.length(); i++) {
                char[] chars = new char[1];
                builder.getChars(i, i + 1, chars, 0);
                writer.write(chars);
            }
        } catch (IOException e) {
            System.err.println("Unable write streams!");
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e){
                System.err.println("Unable to close streams!");
                e.printStackTrace();
            }
        }
    }
}
