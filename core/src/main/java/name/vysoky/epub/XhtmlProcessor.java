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

/**
 * Read XHTML file character by character and extract strings between tags
 * than invoke string processor on extracted text and write modified string.
 */
@SuppressWarnings("unused")
public class XhtmlProcessor {

    private TextProcessor processor;
    private StringBuilder output = new StringBuilder();
    private StringBuilder cache = new StringBuilder();

    private boolean process = false;

    public XhtmlProcessor(TextProcessor processor) {
        this.processor = processor;
    }

    public String process(String input) {
        output = new StringBuilder();
        cache = new StringBuilder();
        for (int i = 0; i < input.length(); i++) processChar(input.charAt(i));
        return output.toString();
    }

    private void processChar(char c) {
        switch (c) {
            case '>':
                output.append(c);
                process = true;
                break;
            case '<':
                process = false;
                output.append(processor.process(cache.toString())); // process and write to output
                cache = new StringBuilder(); // reset cache
                output.append(c);
                break;
            default:
                if (process) cache.append(c); // write to cache for later processing
                else output.append(c);        // write to output directly
                break;
        }
    }
}
