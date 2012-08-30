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
package org.richfaces.cdk.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

import org.w3c.dom.Element;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0">
 * That class represents all faces-config "extension" fields.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class ConfigExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Element> extensions = Lists.newArrayList();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the extensions
     */
    @XmlAnyElement
    public List<Element> getExtensions() {
        return extensions;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param extensions the extensions to set
     */
    public void setExtensions(List<Element> extensions) {
        this.extensions = extensions;
    }
}
