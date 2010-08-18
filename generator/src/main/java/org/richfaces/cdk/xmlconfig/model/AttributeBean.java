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

package org.richfaces.cdk.xmlconfig.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlType(name = "faces-config-attributeType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
        propOrder = {"name", "type", "defaultValue", "suggestedValue", "extension"})
@XmlJavaTypeAdapter(AttributeAdapter.class)
public class AttributeBean extends PropertyBase {

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the name
     */
    @XmlElement(name = "attribute-name", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, required = true)
    public String getName() {
        return super.getName();
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the type
     */
    @XmlElement(name = "attribute-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, required = true)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getType() {
        return super.getType();
    }


    @Override
    public void setType(ClassName type) {
        super.setType(type);
    }

    @Override
    @XmlElement(name = "default-value", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public String getDefaultValue() {
        return super.getDefaultValue();
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        super.setDefaultValue(defaultValue);
    }

    @Override
    @XmlElement(name = "suggested-value", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public String getSuggestedValue() {
        return super.getSuggestedValue();
    }


    @Override
    public void setSuggestedValue(String suggestedValue) {
        super.setSuggestedValue(suggestedValue);
    }

    @Override
    @XmlElement(name = "attribute-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public PropertyExtension getExtension() {
        return super.getExtension();
    }

    @Override
    public void setExtension(PropertyExtension extension) {
        super.setExtension(extension);
    }
}
