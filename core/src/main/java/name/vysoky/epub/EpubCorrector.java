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
    private SmartQuoter smartQuoter;
    private XhtmlProcessor replacingProcessor;
    private XhtmlProcessor quotingProcessor;

    public EpubCorrector(EpubTool epubTool, Locale locale) {
        this.epubTool = epubTool;
        try {
            this.replacingProcessor = new XhtmlProcessor(new TextReplacer(new LocaleReplacementProvider(locale)));
            this.smartQuoter = new SmartQuoter('\u201E','\u201C','\u201A','\u2018');
            this.quotingProcessor = new XhtmlProcessor(smartQuoter);
            logger.debug("Prepared text processor.");
        } catch (Exception e) {
            logger.error("Unable to prepare XHTML processor!", e);
        }
    }

    public void executeReplacer() throws IOException {
        Book book = epubTool.getBook();
        Spine spine = book.getSpine();
        for (SpineReference spineReference : spine.getSpineReferences()) {
            Resource resource = spineReference.getResource();
            String s = new String(resource.getData(), resource.getInputEncoding());
            s = replacingProcessor.process(s);
            epubTool.writeResourceAsString(resource, s);
        }
    }

    public void executeSmartQuoter() throws IOException {
        Book book = epubTool.getBook();
        Spine spine = book.getSpine();
        for (SpineReference spineReference : spine.getSpineReferences()) {
            Resource resource = spineReference.getResource();
            smartQuoter.setDocument(resource.getHref());
            String s = new String(resource.getData(), resource.getInputEncoding());
            s = quotingProcessor.process(s);
            epubTool.writeResourceAsString(resource, s);
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
                String os = replacingProcessor.process(is);
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
