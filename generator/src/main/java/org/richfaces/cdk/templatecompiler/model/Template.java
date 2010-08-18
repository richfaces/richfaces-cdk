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

import java.io.Serializable;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@XmlRootElement(name = "root", namespace = Template.CDK_NAMESPACE)
public class Template implements Serializable {

    public static final String JSTL_CORE_NAMESPACE = "http://richfaces.org/cdk/jstl/core";

    public static final String CDK_NAMESPACE = "http://richfaces.org/cdk/core";

    public static final String CDK_PASS_THROUGH_NAMESPACE = "http://richfaces.org/cdk/ext";

    public static final String COMPOSITE_NAMESPACE = "http://richfaces.org/cdk/jsf/composite";

    public static final String XHTML_EL_NAMESPACE = "http://richfaces.org/cdk/xhtml-el";

    public static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    private static final long serialVersionUID = -6900382133123748812L;

    private String templatePath;

    private CompositeInterface compositeInterface;

    private CompositeImplementation compositeImplementation;

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the interface
     */
    @XmlElement(name = "interface", namespace = COMPOSITE_NAMESPACE)
    public CompositeInterface getInterface() {
        return this.compositeInterface;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param interface1
     *            the interface to set
     */
    public void setInterface(CompositeInterface interface1) {
        this.compositeInterface = interface1;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the implementation
     */
    @XmlElement(name = "implementation", namespace = COMPOSITE_NAMESPACE)
    public CompositeImplementation getImplementation() {
        return this.compositeImplementation;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param implementation
     *            the implementation to set
     */
    public void setImplementation(CompositeImplementation implementation) {
        this.compositeImplementation = implementation;
    }

    public static boolean isDirectiveNamespace(QName name) {
        return isDirectiveNamespace(name.getNamespaceURI());
    }

    public static boolean isDirectiveNamespace(String namespace) {
        return JSTL_CORE_NAMESPACE.equals(namespace) || CDK_NAMESPACE.equals(namespace)
            || CDK_PASS_THROUGH_NAMESPACE.equals(namespace)|| COMPOSITE_NAMESPACE.equals(namespace);
    }

    public static boolean isDefaultNamespace(QName name) {
        return isDefaultNamespace(name.getNamespaceURI());
    }

    public static boolean isDefaultNamespace(String namespace) {
        return XMLConstants.NULL_NS_URI.equals(namespace) || XHTML_EL_NAMESPACE.equals(namespace)
            || XHTML_NAMESPACE.equals(namespace);
    }
}
