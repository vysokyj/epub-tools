package name.vysoky.epub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * DOM error handler using SLF4J.
 *
 * @author Jiri Vysoky
 */
public class EpubErrorHandler implements ErrorHandler {

    final Logger logger = LoggerFactory.getLogger(EpubErrorHandler.class);

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        logger.warn("SAX warning!", exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        logger.error("SAX error!", exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        logger.error("SAX fatal error!", exception);
    }
}
