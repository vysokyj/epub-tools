package name.vysoky.epub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * If this is not set to XML reader, than XHTML DOCTYPE cause parser freeze!
 * @author Jiri Vysoky
 */
public class EpubEntityResolver implements EntityResolver {

    final Logger logger = LoggerFactory.getLogger(EpubEntityResolver.class);

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        logger.info("Resolve entity - publicId: " + publicId + ", systemId: " + systemId);

       return new InputSource(new java.io.StringReader(""));

//        if (publicId.equals("-//W3C//DTD XHTML 1.1//EN") ||
//                systemId.equals("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"))
//            return new InputSource(getClass().getResourceAsStream("/xhtml11.dtd"));
//
//        if (publicId.equals("-//W3C//ELEMENTS XHTML Inline Style 1.0//EN") ||
//                systemId.equals("http://www.w3.org/MarkUp/DTD/xhtml-inlstyle-1.mod"))
//            return new InputSource(getClass().getResourceAsStream("/xhtml-inlstyle-1.mod"));
//        if (systemId.contains("www.w3.org"))
//            return new InputSource(new URL(systemId).openStream());
//
//        logger.warn("Used null input source!");
//        return null;
    }

}
