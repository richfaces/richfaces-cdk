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

import org.apache.cocoon.pipeline.util.dom.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Implements support for shorthand XPointers (= id-based lookup). We treat them here as if they were a pointerpart too.
 *
 * <p>
 * Note that although this is implemented here, this feature depends on the presence of a DTD, and a validating parser.
 * Currently, this means its unuseable within Cocoon.
 */
public final class ShorthandPart implements PointerPart {
    private String shorthand;

    public ShorthandPart(String shorthand) {
        this.shorthand = shorthand;
    }

    public boolean process(XPointerContext xpointerContext) throws SAXException, IOException {
        Document document = xpointerContext.getDocument();
        Element element = document.getElementById(shorthand);

        if (element != null) {
            DOMUtils.stream(element, xpointerContext.getXmlConsumer());

            return true;
        } else {
            if (xpointerContext.getLogger().isDebugEnabled()) {
                xpointerContext.getLogger().debug(
                        "XPointer: found no element with id " + shorthand + " in document " + xpointerContext.getSource());
            }
        }

        return false;
    }
}
