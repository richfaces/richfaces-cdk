/**
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

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.PropertyModel;

/**
 * @author akolonitsky
 * @since Jan 6, 2010
 */
@XmlType(name = "faces-config-converterType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
    propOrder = {"id", "converterForClass", "targetClass", "facesAttributes", "properties", "extension"})
@XmlJavaTypeAdapter(ConverterAdapter.class)
public class ConverterBean extends ElementBeanBase<ConverterBean.ConverterExtension> {

    private ClassName converterForClass;

    private ClassName targetClass;

    private FacesId id;

    @XmlElement(name = "converter-id", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public FacesId getId() {
        return id;
    }

    public void setId(FacesId id) {
        this.id = id;
    }

    @Override
    @XmlElement(name = "property", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, type = PropertyBean.class)
    @XmlJavaTypeAdapter(PropertyAdapter.class)
    public Collection<PropertyModel> getProperties() {
        return super.getProperties();
    }

    @Override
    @XmlElement(name = "attribute", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, type = AttributeBean.class)
    @XmlJavaTypeAdapter(AttributeAdapter.class)
    public Collection<AttributeModel> getFacesAttributes() {
        return super.getFacesAttributes();
    }

    @XmlElement(name = "converter-for-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public ClassName getConverterForClass() {
        return converterForClass;
    }

    public void setConverterForClass(ClassName converterForClass) {
        this.converterForClass = converterForClass;
    }

    @XmlElement(name = "converter-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public ClassName getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(ClassName converterClass) {
        this.targetClass = converterClass;
    }

    @Override
    public void setExtension(ConverterExtension extension) {
        super.setExtension(extension);
    }

    @XmlElement(name = "converter-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, nillable = true)
    @Override
    public ConverterExtension getExtension() {
        return super.getExtension();
    }

    public static class ConverterExtension extends ExtensionBeanBase {

    }
}
