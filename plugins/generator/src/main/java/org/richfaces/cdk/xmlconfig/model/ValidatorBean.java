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
 * @since Jan 13, 2010
 */
@XmlType(name = "faces-config-validatorType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
    propOrder={"id", "targetClass", "facesAttributes", "properties", "extension"})
@XmlJavaTypeAdapter(ValidatorAdapter.class)
public class ValidatorBean extends ElementBeanBase<ValidatorBean.ValidatorExtension> {


    private ClassName targetClass;

    private FacesId id;

    @XmlElement(name = "validator-id", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
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

    @XmlElement(name = "validator-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public ClassName getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(ClassName validatorClass) {
        this.targetClass = validatorClass;
    }

    @Override
    @XmlElement(name = "validator-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public ValidatorExtension getExtension() {
        return super.getExtension();
    }

    @Override
    public void setExtension(ValidatorExtension extension) {
        super.setExtension(extension);
    }

    public static class ValidatorExtension extends ExtensionBeanBase {

    }
}
