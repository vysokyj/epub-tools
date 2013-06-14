package name.vysoky.epub;

import nl.siegmann.epublib.domain.Resource;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Heading element buffer used by table of content generator.
 * @see HeadingElement
 * @author Jiri Vysoky
 */
public class HeadingElementBuffer {
    private int fragmentIdCounter = 1;
    private List<HeadingElement> processed = new ArrayList<HeadingElement>(); // processed roots only
    private HeadingElement[] opened = new HeadingElement[5];

    private byte minLevel = 1;
    private byte maxLevel = (byte) HeadingElement.HEADINGS.length;
    private String[] ignoreClasses = { "tocignore" };

    public void setMinLevel(byte minLevel) {
        //TODO: Check if is between 1 and 6
        this.minLevel = minLevel;
    }

    public void setMaxLevel(byte maxLevel) {
        //TODO: Check if is between 1 and 6
        this.maxLevel = maxLevel;
    }

    public void addElement(Resource resource, Element element) {
        if (!HeadingElement.isHeadingElement(element))
            throw new IllegalArgumentException("Given element is not heading");
        element.removeAttribute("id"); // clean all headings id
        HeadingElement current = new HeadingElement(element);
        if (accept(current)) {
            String fragmentId = "toc-" + Integer.toString(fragmentIdCounter++);
            current.setResource(resource);
            current.setFragmentId(fragmentId);
            HeadingElement parent = findNearestOpenedParentElement(current);
            if (parent == null) processed.add(current);
            else parent.addChild(current);
            setOpenedElement(current);
        }
    }

    public List<HeadingElement> getProcessed() {
        return processed;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Heading tree:\n");
        for (HeadingElement headingElement : processed) write(headingElement, stringBuilder);
        return stringBuilder.toString();
    }

    private boolean accept(HeadingElement headingElement) {
        // check ignore classes
        String elementClass =  headingElement.getElement().getAttribute("class");
        for (String ignoreClass : ignoreClasses)
            if (elementClass != null && elementClass.contains(ignoreClass)) return false;
        // check max and min level limits
        return (headingElement.getLevel() >= minLevel && headingElement.getLevel() <= maxLevel);
    }

    private void setOpenedElement(HeadingElement current) {
        int i = current.getIndex();
        // clear processed
        for (int j = i; j < opened.length; j++) opened[j] = null;
        // set current as last opened
        opened[current.getIndex()] = current;
    }

    private HeadingElement findNearestOpenedParentElement(HeadingElement headingElement) {
        if (headingElement.getIndex() == 0) return null;
        int maxParentIndex =  headingElement.getIndex() - 1;
        for (int i = maxParentIndex; i >= 0; i--) {
            HeadingElement e = opened[i];
            if (e != null) return e;
        }
        return null;
    }


    private void write(HeadingElement headingElement, StringBuilder stringBuilder) {
        for (int i = 0; i < headingElement.getIndex(); i++) stringBuilder.append('-');
        stringBuilder.append(headingElement.getElement().getTextContent());
        stringBuilder.append("\n");
        for (HeadingElement child : headingElement.getChildren()) write(child, stringBuilder);
    }
}
