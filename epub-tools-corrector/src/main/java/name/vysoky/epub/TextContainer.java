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

import java.io.IOException;

/**
 * Used for text containing elements such as a book and its text parts.
 * <p/>
 * Element is represented with some file in filesystem and contains XHTML document for corrections.
 * @see Book
 * @see Part
 */
public interface TextContainer extends FileBased {

    /**
     * Correct text using XHTML corrector.
     * This action invoke book extraction, if book is note yet extracted.
     * @throws IOException common input output error
     */
    public void correct() throws IOException;

    /**
     * Clean and repair with TIDY
     */
    public void tidy() throws IOException;

    /**
     * Find CSS classes in managed XHTML document or documents.
     * @param styleMap style map, key is class name and value is style class handler object
     * @throws java.io.IOException common input output errors
     */
    public void findCSSClasses(StyleReport styleReport) throws IOException;

    /**
     * Remove style with given name from XHTML parts.
     * @param className a name of the style
     * IOException common input output errorsIOException
     */
    public void removeCSSClass(String className) throws IOException;
}
