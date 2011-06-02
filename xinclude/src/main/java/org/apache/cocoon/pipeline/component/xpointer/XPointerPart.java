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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.cocoon.pipeline.component.sax.SAXConsumer;
import org.apache.cocoon.pipeline.util.dom.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

public final class XPointerPart implements PointerPart {
    private static final XPathFactory XPF = XPathFactory.newInstance();
    private String expression;

    public XPointerPart(String expression) {
        this.expression = expression;
    }

    public boolean process(XPointerContext xpointerContext) throws SAXException, IOException {
        Document document = xpointerContext.getDocument();
        XPath xpath = XPF.newXPath();

        xpath.setNamespaceContext(xpointerContext);

        XPathExpression xpathExpression;

        try {
            xpathExpression = xpath.compile(expression);
        } catch (XPathExpressionException e) {
            throw new SAXException("XPointer: expression \"" + expression + "\" is not a valid XPath expression", e);
        }

        try {
            NodeList nodeList = (NodeList) xpathExpression.evaluate(document, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                SAXConsumer consumer = xpointerContext.getXmlConsumer();
                LocatorImpl locator = new LocatorImpl();

                locator.setSystemId(xpointerContext.getSource().toString());
                consumer.setDocumentLocator(locator);

                for (int i = 0; i < nodeList.getLength(); i++) {
                    DOMUtils.stream(nodeList.item(i), consumer);
                }

                return true;
            } else {
                if (xpointerContext.getLogger().isDebugEnabled()) {
                    xpointerContext.getLogger().debug("XPointer: expression \"" + expression + "\" gave no results.");
                }

                return false;
            }
        } catch (XPathExpressionException e) {
            throw new SAXException("XPointer: impossible to select DOM fragment using expression \"" + expression
                    + "\", see nested exception", e);
        }
    }
}
