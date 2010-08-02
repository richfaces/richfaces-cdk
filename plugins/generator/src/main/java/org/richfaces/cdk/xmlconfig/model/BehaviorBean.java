/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
 * @since Jan 21, 2010
 */
@XmlType(name = "faces-config-behaviorType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
    propOrder = {"id", "targetClass", "facesAttributes", "properties", "extension"})
@XmlJavaTypeAdapter(BehaviorAdapter.class)
public class BehaviorBean extends ElementBeanBase<BehaviorBean.BehaviorExtension> {

    private ClassName targetClass;

    private FacesId id;

    @XmlElement(name = "behavior-id", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
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

    @XmlElement(name = "behavior-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public ClassName getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(ClassName behaviorClass) {
        this.targetClass = behaviorClass;
    }

    @Override
    @XmlElement(name = "behavior-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public BehaviorExtension getExtension() {
        return super.getExtension();
    }

    @Override
    public void setExtension(BehaviorExtension extension) {
        super.setExtension(extension);
    }

    public static class BehaviorExtension extends ExtensionBeanBase {
    }
}
