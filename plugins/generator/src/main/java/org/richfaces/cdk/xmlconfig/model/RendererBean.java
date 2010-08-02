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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConfigExtension;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.TagModel;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
@XmlType(name = "faces-config-rendererType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
    propOrder = {"family", "id", "rendererClass", /*"facet",*/"facesAttributes", "extension"})
public class RendererBean extends
    ElementBeanBase<RendererBean.RendererExtension> {

    private FacesId family;
    private ClassName rendererClass;
    private String id;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the id
     */
    @XmlElement(name = "renderer-type", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public String getId() {
        return id;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param id the id to set
     */
    public void setId(String type) {
        this.id = type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the family
     */
    @XmlElement(name = "component-family", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public FacesId getFamily() {
        return family;
    }

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
     * @return the rendererClass
     */
    @XmlElement(name = "renderer-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getRendererClass() {
        return rendererClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param rendererClass the rendererClass to set
     */
    public void setRendererClass(ClassName rendererClass) {
        this.rendererClass = rendererClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the attributes
     */
    @XmlElement(name = "attribute", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, type = AttributeBean.class)
    @XmlJavaTypeAdapter(AttributeAdapter.class)
    public Collection<AttributeModel> getFacesAttributes() {
        return super.getFacesAttributes();
    }


    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the extension
     */
    @XmlElement(name = "renderer-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public RendererExtension getExtension() {
        return super.getExtension();
    }

    @Override
    public void setExtension(RendererExtension extension) {
        super.setExtension(extension);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @author asmirnov@exadel.com
     */
    public static class RendererExtension extends ConfigExtension {
        private TagModel tag;

        private Boolean rendersChildren;

        /**
         * <p class="changed_added_4_0">
         * </p>
         * <p/>
         * TODO
         *
         * @return the tag
         */
//        @XmlElement(name = "tag", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public TagModel getTag() {
            return this.tag;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param tag the tag to set
         */
        public void setTag(TagModel tag) {
            this.tag = tag;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the rendersChildren
         */
        @XmlElement(name = "renders-children", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getRendersChildren() {
            return this.rendersChildren;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param rendersChildren the rendersChildren to set
         */
        public void setRendersChildren(Boolean rendersChildren) {
            this.rendersChildren = rendersChildren;
        }
    }
}
