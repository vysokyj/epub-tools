package name.vysoky.epub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * DOM error listener using SLF4J.
 *
 * @author Jiri Vysoky
 */
public class EpubErrorListener implements ErrorListener {

    final Logger logger = LoggerFactory.getLogger(EpubErrorHandler.class);

    @Override
    public void warning(TransformerException exception) throws TransformerException {
        logger.warn("Transformer warning!", exception);
    }

    @Override
    public void error(TransformerException exception) throws TransformerException {
        logger.error("Transformer error!", exception);
    }

    @Override
    public void fatalError(TransformerException exception) throws TransformerException {
        logger.error("Transformer fatal error!", exception);
    }
}
