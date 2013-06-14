package name.vysoky.epub;

import nl.siegmann.epublib.domain.Resource;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Heading element representation used by table of content generator.
 *
 * @author Jiri Vysoky
 */
public class HeadingElement {
    public static final String[] HEADINGS = {"h1", "h2", "h3", "h4", "h5", "h6"};

    private Resource resource;
    private Element element;
    private String fragmentId;
    private byte level;
    private int index;

    private List<HeadingElement> children = new ArrayList<HeadingElement>();

    public static boolean isHeadingElement(Element element) {
        String s = element.getTagName();
        return s.length() == 2 && s.startsWith("h");
    }

    public HeadingElement(Element element) {
        this(element, null, null);
    }

    public HeadingElement(Element element, Resource resource, String fragmentId) {
        if (!isHeadingElement(element))
            throw new IllegalArgumentException("Given element is not heading!");
        if (fragmentId != null) element.setAttribute("id", fragmentId);
        this.resource = resource;
        this.element = element;
        this.fragmentId = fragmentId;
        this.level = Byte.parseByte(element.getTagName().substring(1));
        this.index = ((int) level) - 1;
    }

    public void addChild(HeadingElement headingElement) {
        children.add(headingElement);
    }

    public byte getLevel() {
        return level;
    }

    public Element getElement() {
        if (fragmentId != null) element.setAttribute("id", fragmentId);
        return element;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(String fragmentId) {
        if (fragmentId != null) element.setAttribute("id", fragmentId);
        this.fragmentId = fragmentId;
    }

    public List<HeadingElement> getChildren() {
        return children;
    }

    /**
     * Returns heading level as array index. So 1st level is zero.
     * @return level as index
     */
    public int getIndex() {
        return index;
    }
}
