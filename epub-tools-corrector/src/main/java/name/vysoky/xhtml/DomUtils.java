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

package name.vysoky.xhtml;

import org.w3c.dom.*;

import java.util.List;
import java.util.Vector;

/**
 * W3C DOM utility methods.
 *
 * @author tfennelly
 * @author Jiri Vysoky
 */
@SuppressWarnings("unused")
public abstract class DomUtils {
    /**
     * Copy child node references from source to target.
     *
     * @param source Source Node.
     * @param target Target Node.
     */
    public static void copyChildNodes(Node source, Node target) {
        List nodeList = DomUtils.copyNodeList(source.getChildNodes());
        int childCount = nodeList.size();
        for (Object aNodeList : nodeList) target.appendChild((Node) aNodeList);
    }

    /**
     * Replace one node with another node.
     *
     * @param newNode New node - added in same location as oldNode.
     * @param oldNode Old node - removed.
     */
    public static void replaceNode(Node newNode, Node oldNode) {


        Node parentNode = oldNode.getParentNode();

        if (parentNode == null) {
            System.out.println("Cannot replace node [" + oldNode + "] with [" + newNode + "]. [" + oldNode + "] has no parent.");
        } else {
            parentNode.replaceChild(newNode, oldNode);
        }
    }

    /**
     * Replace one node with a list of nodes.
     * <p/>
     * Clones the NodeList elements.
     *
     * @param newNodes New nodes - added in same location as oldNode.
     * @param oldNode  Old node - removed.
     */
    public static void replaceNode(NodeList newNodes, Node oldNode) {
        replaceNode(newNodes, oldNode, true);
    }

    /**
     * Replace one node with a list of nodes.
     *
     * @param newNodes New nodes - added in same location as oldNode.
     * @param oldNode  Old node - removed.
     * @param clone    Clone Nodelist Nodes.
     */
    public static void replaceNode(NodeList newNodes, Node oldNode, boolean clone) {


        Node parentNode = oldNode.getParentNode();

        if (parentNode == null) {
            System.out.println("Cannot replace [" + oldNode + "] with a NodeList. [" + oldNode + "] has no parent.");
            return;
        }

        int nodeCount = newNodes.getLength();
        List nodeList = DomUtils.copyNodeList(newNodes);

        if (nodeCount == 0) {
            if (!(parentNode instanceof Document)) {
                parentNode.removeChild(oldNode);
            }
            return;
        }

        if (parentNode instanceof Document) {
            List elements = DomUtils.getElements(newNodes, "*", null);

            if (!elements.isEmpty()) {
                System.out.println("Request to replace the Document root node with a 1+ in length NodeList.  Replacing root node with the first element node from the NodeList.");
                parentNode.removeChild(oldNode);
                parentNode.appendChild((Node) elements.get(0));
            } else {
                System.out.println("Cannot replace document root element with a NodeList that doesn't contain an element node.");
            }
        } else {
            for (int i = 0; i < nodeCount; i++) {
                if (clone) {
                    parentNode.insertBefore(((Node) nodeList.get(i)).cloneNode(true), oldNode);
                } else {
                    parentNode.insertBefore((Node) nodeList.get(i), oldNode);
                }
            }
            parentNode.removeChild(oldNode);
        }
    }

    /**
     * Insert the supplied node before the supplied reference node (refNode).
     *
     * @param newNode Node to be inserted.
     * @param refNode Reference node before which the supplied nodes should
     *                be inserted.
     */
    public static void insertBefore(Node newNode, Node refNode) {


        Node parentNode = refNode.getParentNode();

        if (parentNode == null) {
            System.out.println("Cannot insert [" + newNode + "] before [" + refNode + "]. [" + refNode + "] has no parent.");
            return;
        }

        if (parentNode instanceof Document && newNode.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println("Request to insert an element before the Document root node.  This is not allowed.  Replacing the Document root with the new Node.");
            parentNode.removeChild(refNode);
            parentNode.appendChild(newNode);
        } else {
            parentNode.insertBefore(newNode, refNode);
        }
    }

    /**
     * Insert the supplied nodes before the supplied reference node (refNode).
     *
     * @param newNodes Nodes to be inserted.
     * @param refNode  Reference node before which the supplied nodes should
     *                 be inserted.
     */
    public static void insertBefore(NodeList newNodes, Node refNode) {


        Node parentNode = refNode.getParentNode();

        if (parentNode == null) {
            System.out.println("Cannot insert a NodeList before [" + refNode + "]. [" + refNode + "] has no parent.");
            return;
        }

        int nodeCount = newNodes.getLength();
        List nodeList = DomUtils.copyNodeList(newNodes);

        if (nodeCount == 0) {
            return;
        }

        if (parentNode instanceof Document) {
            List elements = DomUtils.getElements(newNodes, "*", null);

            if (!elements.isEmpty()) {
                System.out.println("Request to insert a NodeList before the Document root node.  Will replace the root element with the 1st element node from the NodeList.");
                parentNode.removeChild(refNode);
                parentNode.appendChild((Node) elements.get(0));
            } else {
                System.out.println("Cannot insert beforen the document root element from a NodeList that doesn't contain an element node.");
            }

            for (int i = 0; i < nodeCount; i++) {
                Node node = (Node) nodeList.get(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    System.out.println("****" + node);
                    parentNode.insertBefore(node, refNode);
                }
            }
        } else {
            for (int i = 0; i < nodeCount; i++) {
                parentNode.insertBefore((Node) nodeList.get(i), refNode);
            }
        }
    }

    /**
     * Rename element.
     *
     * @param element            The element to be renamed.
     * @param replacementElement The tag name of the replacement element.
     * @param keepChildContent   <code>true</code> if the target element'OPTION_WITHOUT_PARAMETER child content
     *                           is to be copied to the replacement element, false if not. Default <code>true</code>.
     * @param keepAttributes     <code>true</code> if the target element'OPTION_WITHOUT_PARAMETER attributes
     *                           are to be copied to the replacement element, false if not. Default <code>true</code>.
     * @return The renamed element.
     */
    public static Element renameElement(Element element, String replacementElement, boolean keepChildContent, boolean keepAttributes) {


        Element replacement = element.getOwnerDocument().createElement(replacementElement);
        if (keepChildContent) {
            DomUtils.copyChildNodes(element, replacement);
        }
        if (keepAttributes) {
            NamedNodeMap attributes = element.getAttributes();
            int attributeCount = attributes.getLength();

            for (int i = 0; i < attributeCount; i++) {
                Attr attribute = (Attr) attributes.item(i);
                replacement.setAttribute(attribute.getName(), attribute.getValue());
            }
        }
        DomUtils.replaceNode(replacement, element);

        return replacement;
    }

    /**
     * Remove the supplied element from its containing document.
     * <p/>
     * Tries to manage scenarios where a request is made to remove the root element.
     * Cannot remove the root element in any of the following situations:
     * <ul>
     * <li>"keepChildren" parameter is false.</li>
     * <li>root element is empty of {@link Node#ELEMENT_NODE} nodes.</li>
     * </ul>
     *
     * @param element      Element to be removed.
     * @param keepChildren Keep child content.
     */
    public static void removeElement(Element element, boolean keepChildren) {


        Node parent = element.getParentNode();
        if (parent == null) {
            System.out.println("Cannot remove element [" + element + "]. [" + element + "] has no parent.");
            return;
        }

        NodeList children = element.getChildNodes();

        if (parent instanceof Document) {
            List childElements = null;

            if (!keepChildren) {
                System.out.println("Cannot remove document root element [" + DomUtils.getName(element) + "] without keeping child content.");
            } else {
                if (children != null && children.getLength() > 0) {
                    childElements = DomUtils.getElements(element, "*", null);
                }

                if (childElements != null && !childElements.isEmpty()) {
                    parent.removeChild(element);
                    parent.appendChild((Element) childElements.get(0));
                } else {
                    System.out.println("Cannot remove empty document root element [" + DomUtils.getName(element) + "].");
                }
            }
        } else {
            if (keepChildren && children != null) {
                DomUtils.insertBefore(children, element);
            }
            parent.removeChild(element);
        }
    }

    /**
     * Remove all child nodes from the supplied node.
     *
     * @param node to be "cleared".
     */
    public static void removeChildren(Node node) {


        NodeList children = node.getChildNodes();
        int nodeCount = children.getLength();

        for (int i = 0; i < nodeCount; i++) {
            node.removeChild(children.item(0));
        }
    }

    /**
     * Copy the nodes of a NodeList into the supplied list.
     * <p/>
     * This is not a clone.  It'OPTION_WITHOUT_PARAMETER just a copy of the node references.
     * <p/>
     * Allows iteration over the Nodelist using the copy in the knowledge that
     * the list will remain the same length.  Using the NodeList can result in problems
     * because elements can get removed from the list while we're iterating over it.
     *
     * @param nodeList Nodelist to copy.
     * @return List copy.
     */
    public static List copyNodeList(NodeList nodeList) {
        Vector copy = new Vector();

        if (nodeList != null) {
            int nodeCount = nodeList.getLength();

            for (int i = 0; i < nodeCount; i++) {
                copy.add(nodeList.item(i));
            }
        }

        return copy;
    }

    /**
     * Append the nodes from the supplied list to the supplied node.
     *
     * @param node  Node to be appended to.
     * @param nodes List of nodes to append.
     */
    public static void appendList(Node node, List nodes) {


        int nodeCount = nodes.size();

        for (int i = 0; i < nodeCount; i++) {
            node.appendChild((Node) nodes.get(i));
        }
    }

    /**
     * Get a boolean attribute from the supplied element.
     *
     * @param element    The element.
     * @param attribName The attribute name.
     * @return True if the attribute value is "true" (case insensitive), otherwise false.
     */
    public static boolean getBooleanAttrib(Element element, String attribName) {


        String attribVal = element.getAttribute(attribName);

        return (attribVal != null ? attribVal.equalsIgnoreCase("true") : false);
    }

    /**
     * Get a boolean attribute from the supplied element.
     *
     * @param element      The element.
     * @param namespaceURI Namespace URI of the required attribute.
     * @param attribName   The attribute name.
     * @return True if the attribute value is "true" (case insensitive), otherwise false.
     */
    public static boolean getBooleanAttrib(Element element, String attribName, String namespaceURI) {


        String attribVal = element.getAttributeNS(namespaceURI, attribName);

        return (attribVal != null ? attribVal.equalsIgnoreCase("true") : false);
    }

    /**
     * Get the parent element of the supplied element having the
     * specified tag name.
     *
     * @param child           Child element.
     * @param parentLocalName Parent element local name.
     * @return The first parent element of "child" having the tagname "parentName",
     *         or null if no such parent element exists.
     */
    public static Element getParentElement(Element child, String parentLocalName) {
        return getParentElement(child, parentLocalName, null);
    }

    /**
     * Get the parent element of the supplied element having the
     * specified tag name.
     *
     * @param child           Child element.
     * @param parentLocalName Parent element local name.
     * @param namespaceURI    Namespace URI of the required parent element,
     *                        or null if a non-namespaced get is to be performed.
     * @return The first parent element of "child" having the tagname "parentName",
     *         or null if no such parent element exists.
     */
    public static Element getParentElement(Element child, String parentLocalName, String namespaceURI) {


        Node parentNode = child.getParentNode();

        while (parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
            Element parentElement = (Element) parentNode;

            if (getName(parentElement).equalsIgnoreCase(parentLocalName)) {
                if (namespaceURI == null) {
                    return parentElement;
                } else if (parentElement.getNamespaceURI().equals(namespaceURI)) {
                    return parentElement;
                }
            }
            parentNode = parentNode.getParentNode();
        }

        return null;
    }

    /**
     * Get the name from the supplied element.
     * <p/>
     * Returns the {@link Node#getLocalName() localName} of the element
     * if set (namespaced element), otherwise the
     * element'OPTION_WITHOUT_PARAMETER {@link Element#getTagName() tagName} is returned.
     *
     * @param element The element.
     * @return The element name.
     */
    public static String getName(Element element) {
        String name = element.getLocalName();

        if (name != null) {
            return name;
        } else {
            return element.getTagName();
        }
    }

    /**
     * Get attribute value, returning <code>null</code> if unset.
     * <p/>
     * Some DOM implementations return an empty re for an unset
     * attribute.
     *
     * @param element       The DOM element.
     * @param attributeName The attribute to get.
     * @return The attribute value, or <code>null</code> if unset.
     */
    public static String getAttributeValue(Element element, String attributeName) {
        return getAttributeValue(element, attributeName, null);
    }

    /**
     * Get attribute value, returning <code>null</code> if unset.
     * <p/>
     * Some DOM implementations return an empty re for an unset
     * attribute.
     *
     * @param element       The DOM element.
     * @param attributeName The attribute to get.
     * @param namespaceURI  Namespace URI of the required attribute, or null
     *                      to perform a non-namespaced get.
     * @return The attribute value, or <code>null</code> if unset.
     */
    public static String getAttributeValue(Element element, String attributeName, String namespaceURI) {


        String attributeValue;

        if (namespaceURI == null) {
            attributeValue = element.getAttribute(attributeName);
        } else {
            attributeValue = element.getAttributeNS(namespaceURI, attributeName);
        }

        if (attributeValue.length() == 0 && !element.hasAttribute(attributeName)) {
            return null;
        }

        return attributeValue;
    }

    public static Node getPreviousSibling(Node node, short nodeType) {
        Node parent = node.getParentNode();
        if (parent == null) {
            System.out.println("Cannot get node [" + node + "] previous sibling. [" + node + "] has no parent.");
            return null;
        }

        NodeList siblings = parent.getChildNodes();
        int siblingCount = siblings.getLength();
        int nodeIndex = 0;

        // Locate the node
        for (int i = 0; i < siblingCount; i++) {
            Node sibling = siblings.item(i);

            if (sibling == node) {
                nodeIndex = i;
                break;
            }
        }

        if (nodeIndex == 0) {
            return null;
        }

        // Wind back to sibling
        for (int i = nodeIndex - 1; i >= 0; i--) {
            Node sibling = siblings.item(i);

            if (sibling.getNodeType() == nodeType) {
                return sibling;
            }
        }

        return null;
    }

    /**
     * Count the DOM nodes of the supplied type (nodeType) before the supplied
     * node, not including the node itself.
     * <p/>
     * Counts the sibling nodes.
     *
     * @param node     Node whose siblings are to be counted.
     * @param nodeType The DOM {@link Node} type of the siblings to be counted.
     * @return The number of siblings of the supplied type before the supplied node.
     */
    public static int countNodesBefore(Node node, short nodeType) {
        Node parent = node.getParentNode();
        if (parent == null) {
            System.out.println("Cannot count nodes before [" + node + "]. [" + node + "] has no parent.");
            return 0;
        }

        NodeList siblings = parent.getChildNodes();
        int count = 0;
        int siblingCount = siblings.getLength();

        for (int i = 0; i < siblingCount; i++) {
            Node sibling = siblings.item(i);

            if (sibling == node) {
                break;
            }
            if (sibling.getNodeType() == nodeType) {
                count++;
            }
        }

        return count;
    }

    /**
     * Count the DOM nodes of the supplied type (nodeType) between the supplied
     * sibling nodes, not including the nodes themselves.
     * <p/>
     * Counts the sibling nodes.
     *
     * @param node1    First sibling node.
     * @param node2    Second sibling node.
     * @param nodeType The DOM {@link Node} type of the siblings to be counted.
     * @return The number of siblings of the supplied type between the supplied
     *         sibling nodes.
     * @throws UnsupportedOperationException if the supplied {@link Node Nodes}
     *                                       don't have the same parent node i.e. are not sibling nodes.
     */
    public static int countNodesBetween(Node node1, Node node2, short nodeType) {
        Node parent1 = node1.getParentNode();
        if (parent1 == null) {
            System.out.println("Cannot count nodes between [" + node1 + "] and [" + node2 + "]. [" + node1 + "] has no parent.");
            return 0;
        }

        Node parent2 = node2.getParentNode();
        if (parent2 == null) {
            System.out.println("Cannot count nodes between [" + node1 + "] and [" + node2 + "]. [" + node2 + "] has no parent.");
            return 0;
        }

        if (parent1 != parent2) {
            System.out.println("Cannot count nodes between [" + node1 + "] and [" + node2 + "]. These nodes do not share the same sparent.");
            return 0;
        }

        int countBeforeNode1 = countNodesBefore(node1, nodeType);
        int countBeforeNode2 = countNodesBefore(node2, nodeType);
        int count = countBeforeNode2 - countBeforeNode1;

        if (node1.getNodeType() == nodeType) {
            count--;
        }

        return count;
    }

    /**
     * Count the DOM nodes before the supplied node, not including the node itself.
     * <p/>
     * Counts the sibling nodes.
     *
     * @param node Node whose siblings are to be counted.
     * @return The number of siblings before the supplied node.
     */
    public static int countNodesBefore(Node node) {
        Node parent = node.getParentNode();
        if (parent == null) {
            System.out.println("Cannot count nodes before [" + node + "]. [" + node + "] has no parent.");
            return 0;
        }

        NodeList siblings = parent.getChildNodes();
        int count = 0;
        int siblingCount = siblings.getLength();

        for (int i = 0; i < siblingCount; i++) {
            Node sibling = siblings.item(i);

            if (sibling == node) {
                break;
            }
            count++;
        }

        return count;
    }

    /**
     * Count the DOM nodes between the supplied sibling nodes, not including
     * the nodes themselves.
     * <p/>
     * Counts the sibling nodes.
     *
     * @param node1 First sibling node.
     * @param node2 Second sibling node.
     * @return The number of siblings between the supplied sibling nodes.
     * @throws UnsupportedOperationException if the supplied {@link Node Nodes}
     *                                       don't have the same parent node i.e. are not sibling nodes.
     */
    public static int countNodesBetween(Node node1, Node node2) {
        Node parent1 = node1.getParentNode();
        if (parent1 == null) {
            System.out.println("Cannot count nodes between [" + node1 + "] and [" + node2 + "]. [" + node1 + "] has no parent.");
            return 0;
        }

        Node parent2 = node2.getParentNode();
        if (parent2 == null) {
            System.out.println("Cannot count nodes between [" + node1 + "] and [" + node2 + "]. [" + node2 + "] has no parent.");
            return 0;
        }

        if (parent1 != parent2) {
            System.out.println("Cannot count nodes between [" + node1 + "] and [" + node2 + "]. These nodes do not share the same sparent.");
            return 0;
        }

        int countBeforeNode1 = countNodesBefore(node1);
        int countBeforeNode2 = countNodesBefore(node2);
        int count = countBeforeNode2 - countBeforeNode1 - 1;

        return count;
    }

    /**
     * Count the DOM element nodes before the supplied node, having the specified
     * tag name, not including the node itself.
     * <p/>
     * Counts the sibling nodes.
     *
     * @param node    Node whose element siblings are to be counted.
     * @param tagName The tag name of the sibling elements to be counted.
     * @return The number of siblings elements before the supplied node with the
     *         specified tag name.
     */
    public static int countElementsBefore(Node node, String tagName) {
        Node parent = node.getParentNode();
        if (parent == null) {
            System.out.println("Cannot count nodes before [" + node + "]. [" + node + "] has no parent.");
            return 0;
        }

        NodeList siblings = parent.getChildNodes();
        int count = 0;
        int siblingCount = siblings.getLength();

        for (int i = 0; i < siblingCount; i++) {
            Node sibling = siblings.item(i);

            if (sibling == node) {
                break;
            }
            if (sibling.getNodeType() == Node.ELEMENT_NODE && ((Element) sibling).getTagName().equals(tagName)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get all the text DOM sibling nodes before the supplied node and
     * concatenate them together into a single String.
     *
     * @param node Text node.
     * @return String containing the concatentated text.
     */
    public static String getTextBefore(Node node) {
        Node parent = node.getParentNode();
        if (parent == null) {
            System.out.println("Cannot get text before node [" + node + "]. [" + node + "] has no parent.");
            return "";
        }

        NodeList siblings = parent.getChildNodes();
        StringBuffer text = new StringBuffer();
        int siblingCount = siblings.getLength();

        for (int i = 0; i < siblingCount; i++) {
            Node sibling = siblings.item(i);

            if (sibling == node) {
                break;
            }
            if (sibling.getNodeType() == Node.TEXT_NODE) {
                text.append(((Text) sibling).getData());
            }
        }

        return text.toString();
    }

    /**
     * Get all the text DOM sibling nodes before the supplied node and
     * concatenate them together into a single String.
     *
     * @param node1 Test node.
     * @return String containing the concatentated text.
     */
    public static String getTextBetween(Node node1, Node node2) {
        Node parent1 = node1.getParentNode();
        if (parent1 == null) {
            System.out.println("Cannot get text between nodes [" + node1 + "] and [" + node2 + "]. [" + node1 + "] has no parent.");
            return "";
        }

        Node parent2 = node2.getParentNode();
        if (parent2 == null) {
            System.out.println("Cannot get text between nodes [" + node1 + "] and [" + node2 + "]. [" + node2 + "] has no parent.");
            return "";
        }

        if (parent1 != parent2) {
            System.out.println("Cannot get text between nodes [" + node1 + "] and [" + node2 + "]. These nodes do not share the same sparent.");
            return "";
        }

        NodeList siblings = parent1.getChildNodes();
        StringBuffer text = new StringBuffer();
        boolean append = false;
        int siblingCount = siblings.getLength();

        for (int i = 0; i < siblingCount; i++) {
            Node sibling = siblings.item(i);

            if (sibling == node1) {
                append = true;
            }
            if (sibling == node2) {
                break;
            }
            if (append && sibling.getNodeType() == Node.TEXT_NODE) {
                text.append(((Text) sibling).getData());
            }
        }

        return text.toString();
    }

    /**
     * Construct the XPath of the supplied DOM Node.
     * <p/>
     * Supports element, comment and cdata sections DOM Node types.
     *
     * @param node DOM node for XPath generation.
     * @return XPath re representation of the supplied DOM Node.
     */
    public static String getXPath(Node node) {


        StringBuffer xpath = new StringBuffer();
        Node parent = node.getParentNode();

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                xpath.append(getXPathToken((Element) node));
                break;
            case Node.COMMENT_NODE:
                int commentNum = DomUtils.countNodesBefore(node, Node.COMMENT_NODE);
                xpath.append("/{COMMENT}[" + commentNum + 1 + "]");
                break;
            case Node.CDATA_SECTION_NODE:
                int cdataNum = DomUtils.countNodesBefore(node, Node.CDATA_SECTION_NODE);
                xpath.append("/{CDATA}[" + cdataNum + 1 + "]");
                break;
            default:
                throw new UnsupportedOperationException("XPath generation for supplied DOM Node type not supported.  Only supports element, comment and cdata section DOM nodes.");
        }

        while (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
            xpath.insert(0, getXPathToken((Element) parent));
            parent = parent.getParentNode();
        }

        return xpath.toString();
    }

    private static String getXPathToken(Element element) {


        String tagName = element.getTagName();
        int count = DomUtils.countElementsBefore(element, tagName);
        String xpathToken;

        if (count > 0) {
            xpathToken = "/" + tagName + "[" + (count + 1) + "]";
        } else {
            xpathToken = "/" + tagName;
        }

        return xpathToken;
    }

    public static int getDepth(Element element) {
        Node parent = element.getParentNode();
        int depth = 0;

        while (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
            depth++;
            parent = parent.getParentNode();
        }

        return depth;
    }

    /**
     * Add literal text to the supplied element.
     *
     * @param element     Target DOM Element.
     * @param literalText Literal text to be added.
     */
    public static void addLiteral(Element element, String literalText) {


        Document document = element.getOwnerDocument();
        Text literal = document.createTextNode(literalText);
        element.appendChild(literal);
    }

    /**
     * Get the child element having the supplied localname, position
     * and namespace.
     * <p/>
     * Can be used instead of XPath.
     *
     * @param parent    Parent element to be searched.
     * @param localname Localname of the element required.
     * @param position  The position of the element relative to other sibling
     *                  elements having the same name (and namespace if specified) e.g. if
     *                  searching for the 2nd &ltinput&gt; element, this param needs to
     *                  have a value of 2.
     * @return The element at the requested position, or null if no such child
     *         element exists on the parent element.
     */
    public static Element getElement(Element parent, String localname, int position) {
        return getElement(parent, localname, position, null);
    }

    /**
     * Get the child element having the supplied localname, position
     * and namespace.
     * <p/>
     * Can be used instead of XPath.
     *
     * @param parent       Parent element to be searched.
     * @param localname    Localname of the element required.
     * @param position     The position of the element relative to other sibling
     *                     elements having the same name (and namespace if specified) e.g. if
     *                     searching for the 2nd &ltinput&gt; element, this param needs to
     *                     have a value of 2.
     * @param namespaceURI Namespace URI of the required element, or null
     *                     if a namespace comparison is not to be performed.
     * @return The element at the requested position, or null if no such child
     *         element exists on the parent element.
     */
    public static Element getElement(Element parent, String localname, int position, String namespaceURI) {
        List elements = getElements(parent, localname, namespaceURI);

        position = Math.max(position, 1);
        if (position > elements.size()) {
            return null;
        }

        return (Element) elements.get(position - 1);
    }

    /**
     * Get the child elements having the supplied localname and namespace.
     * <p/>
     * Can be used instead of XPath.
     *
     * @param parent       Parent element to be searched.
     * @param localname    Localname of the element required.  Supports "*" wildcards.
     * @param namespaceURI Namespace URI of the required element, or null
     *                     if a namespace comparison is not to be performed.
     * @return A list of W3C DOM {@link Element}OPTION_WITHOUT_PARAMETER.  An empty list if no such
     *         child elements exist on the parent element.
     */
    public static List getElements(Element parent, String localname, String namespaceURI) {


        return getElements(parent.getChildNodes(), localname, namespaceURI);
    }

    /**
     * Get the child elements having the supplied localname and namespace.
     * <p/>
     * Can be used instead of XPath.
     *
     * @param nodeList     List of DOM nodes on which to perform the search.
     * @param localname    Localname of the element required.  Supports "*" wildcards.
     * @param namespaceURI Namespace URI of the required element, or null
     *                     if a namespace comparison is not to be performed.
     * @return A list of W3C DOM {@link Element}OPTION_WITHOUT_PARAMETER.  An empty list if no such
     *         child elements exist on the parent element.
     */
    public static List getElements(NodeList nodeList, String localname, String namespaceURI) {


        int count = nodeList.getLength();
        Vector elements = new Vector();

        for (int i = 0; i < count; i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (localname.equals("*") || getName(element).equals(localname)) {
                    // The local name matches the element we're after...
                    if (namespaceURI == null || namespaceURI.equals(element.getNamespaceURI())) {
                        elements.add(element);
                    }
                }
            }
        }

        return elements;
    }
}
