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

package org.richfaces.cdk.xmlconfig.testmodel;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@XmlRootElement(name = "root-config", namespace = Root.HTTP_FOO_BAR_SCHEMA)
public class Root {
    public static final String EXTENSIONS_NAMESPACE = "http://foo.bar/extensions";
    public static final String HTTP_FOO_BAR_SCHEMA = "http://foo.bar/schema";
    private Set<Child> children = Sets.newHashSet();
    private String name;

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the children
     */
    @XmlElement(namespace = HTTP_FOO_BAR_SCHEMA)
    public Set<Child> getChildren() {
        return children;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the name
     */
    @XmlElement(namespace = HTTP_FOO_BAR_SCHEMA)
    public String getName() {
        return name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param children
     *            the children to set
     */
    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
