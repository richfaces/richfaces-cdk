/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.cdk.templatecompiler.model;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
public class ElementsHandler implements DomHandler<ModelElement, DOMResult> {

    private DocumentBuilder builder;

    /**
     * Default constructor.
     * <p/>
     * It is up to a JAXB provider to decide which DOM implementation
     * to use or how that is configured.
     */
    public ElementsHandler() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory
            .newInstance();
// Create Document Builder
        try {
            this.builder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException("No documentBuilderFactory");
        }
    }

    /**
     * Constructor that allows applications to specify which DOM implementation
     * to be used.
     *
     * @param builder must not be null. JAXB uses this {@link DocumentBuilder} to create
     *                a new element.
     */
    public ElementsHandler(DocumentBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException();
        }
        this.builder = builder;
    }

    public DocumentBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(DocumentBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException();
        }
        this.builder = builder;
    }

    public DOMResult createUnmarshaller(ValidationEventHandler errorHandler) {
        if (builder == null) {
            return new DOMResult();
        } else {
            return new DOMResult(builder.newDocument());
        }
    }

    @Override
    public ModelElement getElement(DOMResult rt) {
        Element domElement = getDomElement(rt);
        AnyElement element = JAXB.unmarshal(new DOMSource(domElement), AnyElement.class);
        String prefix = domElement.getPrefix();
        QName name = new QName(domElement.getNamespaceURI(), domElement.getLocalName(),
            null != prefix ? prefix : XMLConstants.DEFAULT_NS_PREFIX);
        element.setName(name);
        return element;
    }

    public Element getDomElement(DOMResult r) {
        // JAXP spec is ambiguous about what really happens in this case,
        // so work defensively
        Node n = r.getNode();
        if (n instanceof Document) {
            return ((Document) n).getDocumentElement();
        }

        if (n instanceof Element) {
            return (Element) n;
        }

        if (n instanceof DocumentFragment) {
            return (Element) n.getChildNodes().item(0);
        }

        // if the result object contains something strange,
        // it is not a user problem, but it is a JAXB provider's problem.
        // That's why we throw a runtime exception.
        throw new IllegalStateException(n.toString());
    }

    @Override
    public Source marshal(ModelElement n, ValidationEventHandler errorHandler) {
        // Parsed template are newer written out back.
        return null;
    }

}
