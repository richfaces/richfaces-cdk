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

package org.richfaces.cdk.xmlconfig;

import org.richfaces.cdk.model.ComponentLibrary;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
public class FacesConfigNamespacePreffixMapper  extends NamespacePrefixMapper  {

    /*
     *  (non-Javadoc)
     * @see com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper#getPreferredPrefix(java.lang.String, java.lang.String, boolean)
     */
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean required) {
        if (ComponentLibrary.FACES_CONFIG_NAMESPACE.equals(namespaceUri)) {
            return "";
        } else if (ComponentLibrary.CDK_EXTENSIONS_NAMESPACE.equals(namespaceUri)) {
            return "cdk";
        }

        return suggestion;
    }
}
