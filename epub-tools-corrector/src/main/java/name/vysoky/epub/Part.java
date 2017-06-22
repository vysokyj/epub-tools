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

import name.vysoky.xhtml.DomUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * XHTML text part included in Book.
 */
public class Part implements TextContainer, FileBased {

    private Book book;
    private File file;
    private Document document;

    public static final String[] UNSUPPORTED_ATTRIBUTES = { "name", "border", "text" };
    public static final String[] STYLE_ATTRIBUTES = { "id", "class", "style" };

    /**
     * XHTML 1.1 DOCTYPE element used in ePub documents.
     */
    public static String EPUB_DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" " +
            "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";

    /**
     * XHTML 1.0 Strict DOCTYPE produced by jTIDY library.
     */
    public static String JTIDY_DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
            "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

    public static String XML_DECLARATION = "<\\?xml version='1\\.0' encoding='utf-8'\\?>";

    public static String ENCODING = "UTF-8";
    public static String EMPTY_STRING = "";

    /**
     * Parametric constructor.
     * @param book parent book
     * @param path path to the text part
     */
    @SuppressWarnings("unused")
    public Part(Book book, String path) {
        this(book, new File(path));
    }

    /**
     * Parametric constructor.
     * @param book parent book
     * @param file path to the text part
     */
    public Part(Book book, File file) {
        this.book = book;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void correct() throws IOException {
        book.getCorrector().correct(getFile());
    }

    public void tidy() {
        File temp = null;
        InputStream is = null;
        try {
            temp = new File(file.getAbsolutePath() + ".tmp");
            FileUtils.copyFile(file, temp);
            is = new BufferedInputStream(new FileInputStream(temp));
            this.document = book.getTidy().parseDOM(is, null); // null = do not save
            removeUnsupportedAttributes(document.getDocumentElement());
            save(); // save explicitly
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                FileUtils.forceDelete(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void findCSSClasses(StyleReport styleReport) {
        if (document == null) tidy();
        findStyleClasses(document.getDocumentElement(), styleReport);
    }

    @Override
    public void removeCSSClass(String className) {
        if (document == null) tidy();
        removeStyleClass(document.getDocumentElement(), className);
        save();
    }

    public void save() {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            book.getTidy().pprint(document, os);
            fixTidyDocumentAfterSave();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns relative part to the text part in the book.
     * @return text part path
     */
    @Override
    public String toString() {
        return book.getRelativePathToBook(file);
    }

    private void findStyleClasses(Element element, StyleReport styleReport) {
        String classAttributeValue = element.getAttribute("class");
        if (classAttributeValue != null && !classAttributeValue.isEmpty())
            for (String className : splitClassAttributeValue(classAttributeValue))
                processFoundClassName(className, styleReport);
        // recursive call
        for (Element child : listChildElements(element)) findStyleClasses(child, styleReport);
    }

    private void removeStyleClass(Element element, String styleClass) {
        String classAttributeValue = element.getAttribute("class");
        if (classAttributeValue != null && classAttributeValue.contains(styleClass)) {
            classAttributeValue = removeStyleClass(classAttributeValue, styleClass);
            if (classAttributeValue.isEmpty()) element.removeAttribute("class");
            else element.setAttribute("class", classAttributeValue);
            if (element.getTagName().equals("span") && hasNoStyleInformation(element))
                DomUtils.removeElement(element, true);
        }
        // recursive call
        for (Element child : listChildElements(element)) removeStyleClass(child, styleClass);
    }

    private boolean hasNoStyleInformation(Element element) {
        for (String attr : STYLE_ATTRIBUTES)
            if (element.getAttribute(attr) != null && !element.getAttribute(attr).isEmpty()) return false;
        return true;
    }

    private List<Element> listChildElements(Element element) {
        List<Element> nodeList = new ArrayList<Element>(element.getChildNodes().getLength());
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node childNode = element.getChildNodes().item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                nodeList.add(childElement);
            }
        }
        return nodeList;
    }

    private void removeUnsupportedAttributes(Element element) {
        for (String attr : UNSUPPORTED_ATTRIBUTES) element.removeAttribute(attr);
        // recursion
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node childNode = element.getChildNodes().item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE)
                removeUnsupportedAttributes((Element) childNode);
        }
    }

    private String[] splitClassAttributeValue(String value) {
        value = value.trim();
        if (value.contains(" "))
           return value.split(" ");
        else
           return new String[] { value };
    }

    private String removeStyleClass(String classAttributeValue, String styleClass) {
        String[] styleClasses = splitClassAttributeValue(classAttributeValue);
        StringBuilder sb = new StringBuilder();
        for (String current : styleClasses) {
            if (current.equals(styleClass)) continue; // skip removed style
            if (sb.length() > 0) sb.append(" ");
            sb.append(current);
        }
        return sb.toString();
    }

    private void processFoundClassName(String className, StyleReport styleReport) {
        if (className == null || className.isEmpty()) return;
        else styleReport.styleClass(this.getFile().getAbsolutePath(), 0, 0, className);
        //TODO: Implement rows and columns.
    }

    /**
     * Tool jTIDY work only with XHTML 1.0 and custom document type is problematic.
     * This method read file to memory, replace document type and save it back to the same file.
     * @throws IOException common input output error
     */
    private void fixTidyDocumentAfterSave() throws IOException {
        String content = IOUtils.toString(new FileInputStream(getFile()), ENCODING);
        content = content.replaceFirst(XML_DECLARATION, EMPTY_STRING);
        content = content.replaceAll(JTIDY_DOCTYPE, EPUB_DOCTYPE);
        content = (content.startsWith("\n")) ? content.substring(1) : content; // remove empty line at start
        IOUtils.write(content, new FileOutputStream(getFile()), ENCODING);
    }
}
