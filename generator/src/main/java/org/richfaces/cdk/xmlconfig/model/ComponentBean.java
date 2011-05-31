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

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.FacetModel;
import org.richfaces.cdk.model.PropertyModel;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
@XmlType(name = "faces-config-componentType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, propOrder = { "id",
        "targetClass", "facets", "facesAttributes", "properties", "extension" })
@XmlJavaTypeAdapter(ComponentAdapter.class)
public class ComponentBean extends ElementBeanBase<ComponentBean.ComponentExtension> {
    private List<FacetModel> facets = Lists.newArrayList();
    private ClassName targetClass;
    private FacesId id;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the id
     */
    @XmlElement(name = "component-type", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, required = true)
    public FacesId getId() {
        return id;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param id the id to set
     */
    public void setId(FacesId type) {
        this.id = type;
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

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the targetClass
     */
    @XmlElement(name = "component-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getTargetClass() {
        return targetClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param className the targetClass to set
     */
    public void setTargetClass(ClassName className) {
        this.targetClass = className;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the facets
     */
    @XmlElement(name = "facet", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(FacetAdapter.class)
    public List<FacetModel> getFacets() {
        return facets;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param facets the facets to set
     */
    public void setFacets(List<FacetModel> facets) {
        this.facets = facets;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the extension
     */
    @XmlElement(name = "component-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @Override
    public ComponentExtension getExtension() {
        return super.getExtension();
    }

    @Override
    public void setExtension(ComponentExtension extension) {
        super.setExtension(extension);
    }

    public static class ComponentExtension extends TagExtensionBase {
        private FacesId rendererType;
        private List<EventModel> events = Lists.newArrayList();
        private FacesId family;

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param family the family to set
         */
        public void setFamily(FacesId family) {
            this.family = family;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the family
         */
        @XmlElement(name = "component-family", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public FacesId getFamily() {
            return family;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param rendererType the rendererTypes to set
         */
        @XmlElement(name = "renderer-type", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public void setRendererType(FacesId rendererType) {
            this.rendererType = rendererType;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the rendererTypes
         */
        public FacesId getRendererType() {
            return rendererType;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the events
         */
        @XmlElement(name = "fires", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        @XmlJavaTypeAdapter(EventAdapter.class)
        public List<EventModel> getEvents() {
            return events;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param events the events to set
         */
        public void setEvents(List<EventModel> events) {
            this.events = events;
        }
    }
}
