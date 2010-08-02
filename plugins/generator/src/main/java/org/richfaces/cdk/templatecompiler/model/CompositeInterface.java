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
import java.util.List;

import javax.faces.render.RenderKitFactory;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.xmlconfig.model.ClassAdapter;
import org.richfaces.cdk.xmlconfig.model.FacesIdAdapter;

import com.google.common.collect.Lists;


/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlRootElement(name = "interface", namespace = Template.COMPOSITE_NAMESPACE)
public class CompositeInterface implements Serializable {

    private static final long serialVersionUID = -5578359507253872500L;

    private FacesId componentFamily;

    private List<Attribute> attributes=Lists.newArrayList();

    private List<ResourceDependency> resourceDependencies=Lists.newArrayList();

    private List<ImportAttributes> attributesImports=Lists.newArrayList();
    
    private String renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT;

    private ClassName javaClass;

    private ClassName baseClass;

    private FacesId rendererType;

    private Boolean rendersChildren = null;

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the family
     */
    @XmlJavaTypeAdapter(FacesIdAdapter.class)
    @XmlElement(name = "component-family", namespace = Template.CDK_NAMESPACE)
    public FacesId getComponentFamily() {
        return this.componentFamily;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param family the family to set
     */
    public void setComponentFamily(FacesId family) {
        this.componentFamily = family;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the attributes
     */
    @XmlElement(name = "attribute", namespace = Template.COMPOSITE_NAMESPACE)
    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the resourceDependencies
     */
//    @XmlElementWrapper(name = "resource-dependencies", namespace = Template.CDK_NAMESPACE)
    @XmlElement(name = "resource-dependency", namespace = Template.CDK_NAMESPACE)
    public List<ResourceDependency> getResourceDependencies() {
        return resourceDependencies;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param resourceDependencies the resourceDependencies to set
     */
    public void setResourceDependencies(
        List<ResourceDependency> resourceDependencies) {
        this.resourceDependencies = resourceDependencies;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the renderKitId
     */
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlElement(name = "renderkit-id", namespace = Template.CDK_NAMESPACE)
    public String getRenderKitId() {
        return this.renderKitId;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param renderKitId the renderKitId to set
     */
    public void setRenderKitId(String renderKitId) {
        this.renderKitId = renderKitId;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the javaClass
     */
    @XmlJavaTypeAdapter(ClassAdapter.class)
    @XmlElement(name = "class", namespace = Template.CDK_NAMESPACE)
    public ClassName getJavaClass() {
        return this.javaClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param javaClass the javaClass to set
     */
    public void setJavaClass(ClassName javaClass) {
        this.javaClass = javaClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the rendererType
     */
    @XmlJavaTypeAdapter(FacesIdAdapter.class)
    @XmlElement(name = "renderer-type", namespace = Template.CDK_NAMESPACE)
    public FacesId getRendererType() {
        return this.rendererType;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param rendererType the rendererType to set
     */
    public void setRendererType(FacesId rendererType) {
        this.rendererType = rendererType;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the baseClass
     */
    @XmlJavaTypeAdapter(ClassAdapter.class)
    @XmlElement(name = "superclass", namespace = Template.CDK_NAMESPACE)
    public ClassName getBaseClass() {
        return this.baseClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param baseClass the baseClass to set
     */
    public void setBaseClass(ClassName baseClass) {
        this.baseClass = baseClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the rendersChildren
     */
    @XmlElement(name = "renders-children", namespace = Template.CDK_NAMESPACE)
    public Boolean getRendersChildren() {
        return rendersChildren;
    }

    /**
     * @param rendersChildren the rendersChildren to set
     */
    public void setRendersChildren(Boolean rendersChildren) {
        this.rendersChildren = rendersChildren;
    }
    
    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the attributesImports
     */
    @XmlElement(name = "import-attributes", namespace = Template.CDK_NAMESPACE)
    public List<ImportAttributes> getAttributesImports() {
        return attributesImports;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param attributesImports the attributesImports to set
     */
    public void setAttributesImports(List<ImportAttributes> attributesImports) {
        this.attributesImports = attributesImports;
    }
    
}
