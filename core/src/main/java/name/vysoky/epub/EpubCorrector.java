package name.vysoky.epub;

import name.vysoky.re.LocaleReplacementProvider;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

/**
 * Text corrector.
 * @author Jiri Vysoky
 */
public class EpubCorrector {

    final Logger logger = LoggerFactory.getLogger(EpubCorrector.class);

    private EpubTool epubTool;
    private XhtmlProcessor processor;

    public EpubCorrector(EpubTool epubTool, Locale locale) {
        this.epubTool = epubTool;
        try {
            this.processor = new XhtmlProcessor(new TextReplacer(new LocaleReplacementProvider(locale)));
            logger.debug("Prepared text processor.");
        } catch (Exception e) {
            logger.error("Unable to prepare XHTML processor!", e);
        }
    }

    public void correct() throws IOException {
        Book book = epubTool.getBook();
        Spine spine = book.getSpine();
        for (SpineReference spineReference : spine.getSpineReferences()) {
            Resource resource = spineReference.getResource();
            String is = new String(resource.getData(), resource.getInputEncoding());
            String os = processor.process(is);
            epubTool.writeResourceAsString(resource, os);
        }
    }


    /* Old code using DOM. */

    /*

    private Document processPart(Resource resource) {
        try {
            Document document = epubTool.readDocument(resource);
            //Element root = document.getDocumentElement();
            Element root = epubTool.getBodyElement(document);
            processElement(root);
            return document;
        } catch (Exception e) {
            logger.error("Unable to process part!", e);
            return null;
        }
    }


    private void processElement(Element element) {
        NodeList nodeList = element.getChildNodes();
        // process texts
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                Text text = (Text) node;
                String is = text.getData();
                String os = processor.process(is);
                if (!is.equals(os)) {
                    logger.info(is + " -> " + os);
                    text.setData(os);
                }
            }
        }
        // recursion
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                processElement(child);
            }
        }
    }

    */
}
