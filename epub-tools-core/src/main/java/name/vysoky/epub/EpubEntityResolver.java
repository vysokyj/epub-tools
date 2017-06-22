package name.vysoky.epub;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * If this is not set to XML reader, than XHTML DOCTYPE cause parser freeze!
 * @author Jiri Vysoky
 */
public class EpubEntityResolver implements EntityResolver {
    private String previousLocation;

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String resource;
        if (systemId.startsWith("http:")) {
            URL url = new URL(systemId);
            resource = "dtd/" + url.getHost() + url.getPath();
            previousLocation = resource.substring(0, resource.lastIndexOf('/'));
        } else {
            resource = previousLocation + systemId.substring(systemId.lastIndexOf('/'));
        }
        InputStream stream = EpubEntityResolver.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) throw new IOException("Unable to open resource as stream! resource='" + resource + "'");
        return new InputSource(stream);
    }
}
