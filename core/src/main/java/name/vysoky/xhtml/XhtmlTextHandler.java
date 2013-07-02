package name.vysoky.xhtml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Extract plain text from XHTML document. Extract only content of body element.
 *
 * @author Jiri Vysoky
 */
public class XhtmlTextHandler extends DefaultHandler {

    private static String BODY_TAG = "body";
    private static final Set<String> LINE_TAGS = new HashSet<String>(
            Arrays.asList(new String[] {"h1", "h2", "h3", "h4", "h5", "h6", "p"})
    );
    private StringBuilder sb;
    private boolean read = false;

    /**
     * Parametric constructor.
     * @param sb string builder used for extracted text
     */
    public XhtmlTextHandler(StringBuilder sb) {
        if (sb == null) throw new NullPointerException("String builder must not be null!");
        this.sb = sb;
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals(BODY_TAG)) read = true;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals(BODY_TAG)) read = false;
        if (LINE_TAGS.contains(localName)) sb.append('\n');
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (read) for (int i = start; i < start + length; i++) process(ch[i]);
    }

    private void process(char c) {
        char last = (sb.length() > 0) ? sb.charAt(sb.length() - 1) : '\n';
        if (c == '\t') c = ' ';                 // convert tab to space
        if (c == '\n' && last == '\n') return;  // skip double lines
        if (c == ' '  && last == ' ' ) return;  // skip double spaces
        if (c == ' '  && last == '\n') return;  // skip spaces after line
        sb.append(c);
    }
}
