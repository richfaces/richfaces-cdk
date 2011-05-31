/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cocoon.pipeline.util.dom;

import java.util.Map.Entry;

import org.apache.cocoon.pipeline.component.sax.SAXConsumer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

final class DOMStreamer {
    private ElementInfo currentElementInfo;
    /** Counter used when generating new namespace prefixes. */
    private int newPrefixCounter;
    private SAXConsumer xmlConsumer;

    public DOMStreamer(SAXConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    /**
     * Start the production of SAX events.
     */
    public void stream(Node node) throws SAXException {

        // Start document only if we're streaming a document
        boolean isDoc = node.getNodeType() == Node.DOCUMENT_NODE;

        if (isDoc) {
            xmlConsumer.startDocument();
        }

        Node top = node;

        while (null != node) {
            startNode(node);

            Node nextNode = node.getFirstChild();

            while (null == nextNode) {
                endNode(node);

                if (top.equals(node)) {
                    break;
                }

                nextNode = node.getNextSibling();

                if (null == nextNode) {
                    node = node.getParentNode();

                    if ((null == node) || (top.equals(node))) {
                        if (null != node) {
                            endNode(node);
                        }

                        nextNode = null;

                        break;
                    }
                }
            }

            node = nextNode;
        }

        if (isDoc) {
            xmlConsumer.endDocument();
        }
    }

    private void startNode(Node node) throws SAXException {
        switch (node.getNodeType()) {
            case Node.COMMENT_NODE:

                // ignore comment
                break;

            case Node.DOCUMENT_FRAGMENT_NODE:

                // ??;
                break;

            case Node.DOCUMENT_NODE:
                break;

            case Node.ELEMENT_NODE:
                NamedNodeMap atts = node.getAttributes();
                int nAttrs = atts.getLength();

                // create a list of localy declared namespace prefixes
                currentElementInfo = new ElementInfo(currentElementInfo);

                for (int i = 0; i < nAttrs; i++) {
                    Node attr = atts.item(i);
                    String attrName = attr.getNodeName();

                    if (attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
                        int index = attrName.indexOf(":");
                        String prefix = index < 0 ? "" : attrName.substring(index + 1);

                        currentElementInfo.put(prefix, attr.getNodeValue());
                    }
                }

                String namespaceURI = node.getNamespaceURI();
                String prefix = node.getPrefix();
                String localName = node.getLocalName();

                if (localName == null) {

                    // this is an element created with createElement instead of createElementNS
                    String[] prefixAndLocalName = getPrefixAndLocalName(node.getNodeName());

                    prefix = prefixAndLocalName[0];
                    localName = prefixAndLocalName[1];

                    // note: if prefix is null, there can still be a default namespace...
                    namespaceURI = getNamespaceForPrefix(prefix, (Element) node);
                }

                if (namespaceURI != null) {

                    // no prefix means: make this the default namespace
                    if (prefix == null) {
                        prefix = "";
                    }

                    // check that is declared
                    String uri = currentElementInfo.findNamespaceURI(prefix);

                    if (uri != null && uri.equals(namespaceURI)) {

                        // System.out.println("namespace is declared");
                        // prefix is declared correctly, do nothing
                    } else if (uri != null) {

                        // System.out.println("prefix is declared with other namespace, overwriting it");
                        // prefix exists but is bound to another namespace, overwrite it
                        currentElementInfo.put(prefix, namespaceURI);
                    } else {

                        // System.out.println("prefix is not yet declared, declaring it now");
                        currentElementInfo.put(prefix, namespaceURI);
                    }
                } else {

                    // element has no namespace
                    // check if there is a default namespace, if so undeclare it
                    String uri = currentElementInfo.findNamespaceURI("");

                    if (uri != null && uri.length() > 0) {

                        // System.out.println("undeclaring default namespace");
                        currentElementInfo.put("", "");
                    }
                }

                // SAX uses empty string to denote no namespace, while DOM uses null.
                if (namespaceURI == null) {
                    namespaceURI = "";
                }

                String qName;

                if (prefix != null && prefix.length() > 0) {
                    qName = prefix + ":" + localName;
                } else {
                    qName = localName;
                }

                // make the attributes
                AttributesImpl newAttrs = new AttributesImpl();

                for (int i = 0; i < nAttrs; i++) {
                    Node attr = atts.item(i);
                    String attrName = attr.getNodeName();
                    String assignedAttrPrefix = null;

                    // only do non-namespace attributes
                    if (!(attrName.equals("xmlns") || attrName.startsWith("xmlns:"))) {
                        String attrPrefix;
                        String attrLocalName;
                        String attrNsURI;

                        if (attr.getLocalName() == null) {

                            // this is an attribute created with setAttribute instead of setAttributeNS
                            String[] prefixAndLocalName = getPrefixAndLocalName(attrName);

                            attrPrefix = prefixAndLocalName[0];

                            // the statement below causes the attribute to keep its prefix even if it is not
                            // bound to a namespace (to support pre-namespace XML).
                            assignedAttrPrefix = attrPrefix;
                            attrLocalName = prefixAndLocalName[1];

                            // note: if prefix is null, the attribute has no namespace (namespace defaulting
                            // does not apply to attributes)
                            if (attrPrefix != null) {
                                attrNsURI = getNamespaceForPrefix(attrPrefix, (Element) node);
                            } else {
                                attrNsURI = null;
                            }
                        } else {
                            attrLocalName = attr.getLocalName();
                            attrPrefix = attr.getPrefix();
                            attrNsURI = attr.getNamespaceURI();
                        }

                        if (attrNsURI != null) {
                            String declaredUri = currentElementInfo.findNamespaceURI(attrPrefix);

                            // if the prefix is null, or the prefix has not been declared, or conflicts
                            // with an in-scope binding
                            if (declaredUri == null || !declaredUri.equals(attrNsURI)) {
                                String availablePrefix = currentElementInfo.findPrefix(attrNsURI);

                                if (availablePrefix != null && !availablePrefix.equals("")) {
                                    assignedAttrPrefix = availablePrefix;
                                } else {
                                    if (attrPrefix != null && declaredUri == null) {

                                        // prefix is not null and is not yet declared: declare it
                                        assignedAttrPrefix = attrPrefix;
                                        currentElementInfo.put(assignedAttrPrefix, attrNsURI);
                                    } else {

                                        // attribute has no prefix (which is not allowed for namespaced attributes) or
                                        // the prefix is already bound to something else: generate a new prefix
                                        newPrefixCounter++;
                                        assignedAttrPrefix = "NS" + newPrefixCounter;
                                        currentElementInfo.put(assignedAttrPrefix, attrNsURI);
                                    }
                                }
                            } else {
                                assignedAttrPrefix = attrPrefix;
                            }
                        }

                        String assignedAttrNsURI = attrNsURI != null ? attrNsURI : "";
                        String attrQName;

                        if (assignedAttrPrefix != null) {
                            attrQName = assignedAttrPrefix + ":" + attrLocalName;
                        } else {
                            attrQName = attrLocalName;
                        }

                        newAttrs.addAttribute(assignedAttrNsURI, attrLocalName, attrQName, "CDATA", attr.getNodeValue());
                    }
                }

                // add local namespace declaration and fire startPrefixMapping events
                if (currentElementInfo.namespaceDeclarations != null && currentElementInfo.namespaceDeclarations.size() > 0) {
                    for (Entry<String, String> entry : currentElementInfo.namespaceDeclarations.entrySet()) {
                        String pr = (String) entry.getKey();
                        String ns = (String) entry.getValue();

                        // the following lines enable the creation of explicit xmlns attributes
                        // String pr1 = pr.equals("") ? "xmlns" : pr;
                        // String qn = pr.equals("") ? "xmlns" : "xmlns:" + pr;
                        // newAttrs.addAttribute("", pr1, qn, "CDATA", ns);
                        // System.out.println("starting prefix mapping  for prefix " + pr + " for " + ns);
                        xmlConsumer.startPrefixMapping(pr, ns);
                    }
                }

                xmlConsumer.startElement(namespaceURI, localName, qName, newAttrs);
                currentElementInfo.localName = localName;
                currentElementInfo.namespaceURI = namespaceURI;
                currentElementInfo.qName = qName;

                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                ProcessingInstruction pi = (ProcessingInstruction) node;

                xmlConsumer.processingInstruction(pi.getNodeName(), pi.getData());

                break;

            case Node.CDATA_SECTION_NODE:
                if (xmlConsumer != null) {
                    xmlConsumer.startCDATA();
                }

                dispatchChars(node);

                if (xmlConsumer != null) {
                    xmlConsumer.endCDATA();
                }

                break;

            case Node.TEXT_NODE:
                dispatchChars(node);

                break;

            case Node.ENTITY_REFERENCE_NODE:
                EntityReference eref = (EntityReference) node;

                if (xmlConsumer != null) {
                    xmlConsumer.startEntity(eref.getNodeName());
                } else {

                    // warning("Can not output entity to a pure SAX xmlConsumer");
                }

                break;

            default:
        }
    }

    private void endNode(Node node) throws SAXException {
        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                break;

            case Node.ELEMENT_NODE:
                xmlConsumer.endElement(currentElementInfo.namespaceURI, currentElementInfo.localName, currentElementInfo.qName);

                // generate endPrefixMapping events if needed
                if (currentElementInfo.namespaceDeclarations != null && currentElementInfo.namespaceDeclarations.size() > 0) {
                    for (Entry<String, String> entry : currentElementInfo.namespaceDeclarations.entrySet()) {
                        xmlConsumer.endPrefixMapping((String) entry.getKey());
                    }
                }

                currentElementInfo = currentElementInfo.parent;

                break;

            case Node.CDATA_SECTION_NODE:
                break;

            case Node.ENTITY_REFERENCE_NODE:
                EntityReference eref = (EntityReference) node;

                if (xmlConsumer != null) {
                    xmlConsumer.endEntity(eref.getNodeName());
                }

                break;

            default:
        }
    }

    private void dispatchChars(Node node) throws SAXException {
        final String data = ((Text) node).getData();

        if (data != null) {
            xmlConsumer.characters(data.toCharArray(), 0, data.length());
        }
    }

    /**
     * Searches the namespace for a given namespace prefix starting from a given Element.
     *
     * <p>
     * Note that this resolves the prefix in the orginal DOM-tree, not in the {@link ElementInfo} objects. This is used to
     * resolve prefixes of elements or attributes created with createElement or setAttribute instead of createElementNS or
     * setAttributeNS.
     *
     * <p>
     * The code in this method is largely based on org.apache.xml.utils.DOMHelper.getNamespaceForPrefix() (from Xalan).
     *
     * @param prefix the prefix to look for, can be empty or null to find the default namespace
     *
     * @return the namespace, or null if not found.
     */
    private String getNamespaceForPrefix(String prefix, Element namespaceContext) {
        if (prefix == null) {
            prefix = "";
        }

        String namespace = null;

        if (prefix.equals("xml")) {
            namespace = "http://www.w3.org/XML/1998/namespace";
        } else if (prefix.equals("xmlns")) {
            namespace = "http://www.w3.org/2000/xmlns/";
        } else {

            // Attribute name for this prefix's declaration
            String declname = (prefix.length() == 0) ? "xmlns" : "xmlns:" + prefix;

            // Scan until we run out of Elements or have resolved the namespace
            Node parent = namespaceContext;
            int type = -1;

            if (parent != null) {
                type = parent.getNodeType();
            }

            while (parent != null && namespace == null && (type == Node.ELEMENT_NODE || type == Node.ENTITY_REFERENCE_NODE)) {
                if (type == Node.ELEMENT_NODE) {
                    Attr attr = ((Element) parent).getAttributeNode(declname);

                    if (attr != null) {
                        namespace = attr.getNodeValue();

                        break;
                    }
                }

                parent = parent.getParentNode();

                if (parent != null) {
                    type = parent.getNodeType();
                }
            }
        }

        return namespace;
    }

    /**
     * Splits a nodeName into a prefix and a localName
     *
     * @return an array containing two elements, the first one is the prefix (can be null), the second one is the localName
     */
    private String[] getPrefixAndLocalName(String nodeName) {
        String prefix;
        String localName;
        int colonPos = nodeName.indexOf(":");

        if (colonPos != -1) {
            prefix = nodeName.substring(0, colonPos);
            localName = nodeName.substring(colonPos + 1, nodeName.length());
        } else {
            prefix = null;
            localName = nodeName;
        }

        return new String[] { prefix, localName };
    }
}
