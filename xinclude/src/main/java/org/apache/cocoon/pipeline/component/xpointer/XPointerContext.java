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
package org.apache.cocoon.pipeline.component.xpointer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import org.apache.cocoon.pipeline.component.sax.SAXConsumer;
import org.apache.cocoon.pipeline.util.dom.DOMUtils;
import org.richfaces.cdk.Logger;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XPointerContext implements NamespaceContext {
    private static final String XML = "xml";
    private static final String XMLNS = "xmlns";
    private static final String XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";
    private static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    private final Logger logger;
    private Map<String, String> prefixes = new HashMap<String, String>();
    private Document document;
    private final EntityResolver resolver;
    private InputSource source;
    private String xPointer;
    private SAXConsumer xmlConsumer;

    public XPointerContext(String xPointer, InputSource source, SAXConsumer xmlConsumer, EntityResolver resolver, Logger log) {
        this.xPointer = xPointer;
        this.source = source;
        this.xmlConsumer = xmlConsumer;
        this.resolver = resolver;
        this.logger = log;
    }

    public String getXPointer() {
        return xPointer;
    }

    public InputSource getSource() {
        return source;
    }

    public SAXConsumer getXmlConsumer() {
        return xmlConsumer;
    }

    public Logger getLogger() {
        return logger;
    }

    public Document getDocument() throws IOException, SAXException {
        if (document == null) {
            document = DOMUtils.toDOM(source, resolver);
        }

        return document;
    }

    public void addPrefix(String prefix, String namespaceURI) {
        if (prefix.equalsIgnoreCase(XML) || prefix.equals(XMLNS)) {
            return;
        }

        if (namespaceURI.equals(XML_NAMESPACE) || namespaceURI.equals(XMLNS_NAMESPACE)) {
            return;
        }

        prefixes.put(prefix, namespaceURI);
    }

    public String getNamespaceURI(String prefix) {
        return prefixes.get(prefix);
    }

    // This method isn't necessary for XPath processing.
    public String getPrefix(String namespaceURI) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    // This method isn't necessary for XPath processing either.
    public Iterator getPrefixes(String namespaceURI) {
        throw new UnsupportedOperationException();
    }
}
