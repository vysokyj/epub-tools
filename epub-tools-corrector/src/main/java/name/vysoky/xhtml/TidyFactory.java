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

import org.w3c.tidy.Tidy;

public class TidyFactory {

    public static Tidy getDefaultTidy() {
        Tidy tidy = new Tidy();
        tidy.setTidyMark(false);
        tidy.setXHTML(true);
        //tidy.setDocType(Part.DOCTYPE); // malfunction
        tidy.setDocType("strict");
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8"); // disable diacritics entities
        tidy.setSmartIndent(false);
        tidy.setWraplen(0);
        tidy.setTabsize(4);
        tidy.setBreakBeforeBR(false);
        tidy.setQuiet(true);
        return tidy;
    }
}
