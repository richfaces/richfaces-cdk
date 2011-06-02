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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class ElementInfo {
    Map<String, String> namespaceDeclarations = null;
    String localName;
    String namespaceURI;
    ElementInfo parent;
    String qName;

    public ElementInfo(ElementInfo parent) {
        this.parent = parent;
    }

    /**
     * Declare a new namespace prefix on this element, possibly overriding an existing one.
     */
    public void put(String prefix, String namespaceURI) {
        if (namespaceDeclarations == null) {
            namespaceDeclarations = new HashMap<String, String>();
        }

        namespaceDeclarations.put(prefix, namespaceURI);
    }

    /**
     * Finds a prefix declared on this element.
     */
    public String getPrefix(String namespaceURI) {
        if (namespaceDeclarations == null || namespaceDeclarations.size() == 0) {
            return null;
        }

        // note: there could be more than one prefix for the same namespaceURI, but
        // we return the first found one.
        for (Entry<String, String> entry : namespaceDeclarations.entrySet()) {
            if (entry.getValue().equals(namespaceURI)) {
                return (String) entry.getKey();
            }
        }

        return null;
    }

    /**
     * Finds a namespace URI declared on this element.
     */
    public String getNamespaceURI(String prefix) {
        if (namespaceDeclarations == null || namespaceDeclarations.size() == 0) {
            return null;
        }

        return (String) namespaceDeclarations.get(prefix);
    }

    /**
     * Finds a prefix declaration on this element or containing elements.
     */
    public String findPrefix(String namespaceURI) {
        if (namespaceDeclarations != null && namespaceDeclarations.size() != 0) {
            String prefix = getPrefix(namespaceURI);

            if (prefix != null) {
                return prefix;
            }
        }

        if (parent != null) {
            return parent.findPrefix(namespaceURI);
        }

        return null;
    }

    /**
     * Finds a namespace declaration on this element or containing elements.
     */
    public String findNamespaceURI(String prefix) {
        if (namespaceDeclarations != null && namespaceDeclarations.size() != 0) {
            String uri = (String) namespaceDeclarations.get(prefix);

            if (uri != null) {
                return uri;
            }
        }

        if (parent != null) {
            return parent.findNamespaceURI(prefix);
        }

        return null;
    }
}
