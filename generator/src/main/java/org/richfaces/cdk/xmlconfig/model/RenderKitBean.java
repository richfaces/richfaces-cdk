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

import java.util.List;

import javax.faces.render.RenderKitFactory;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.BehaviorRendererModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConfigExtension;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.RendererModel;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlType(
        name = "faces-config-render-kitType",
        namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
        propOrder = {"id", "targetClass", "renderers", "behaviorRenderers", "extension"})
public class RenderKitBean extends ExtensibleBean<RenderKitBean.RenderKitExtension>  {

    private List<RendererModel> renderers = Lists.newArrayList();
    private List<BehaviorRendererModel> behaviorRenderers = Lists.newArrayList();
    private ClassName targetClass;
    private FacesId key = new FacesId(RenderKitFactory.HTML_BASIC_RENDER_KIT);


    @XmlElement(name = "render-kit-id", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(FacesIdAdapter.class)
    public FacesId getId() {
        return key;
    }

    public void setId(FacesId key) {
        this.key = key;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the targetClass
     */
    @XmlElement(name = "render-kit-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getTargetClass() {
        return targetClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param targetClass the targetClass to set
     */
    public void setTargetClass(ClassName renderkitClass) {
        this.targetClass = renderkitClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the renderers
     */
    @XmlElement(name = "renderer", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(RendererAdapter.class)
    public List<RendererModel> getRenderers() {
        return renderers;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param renderers the renderers to set
     */
    public void setRenderers(List<RendererModel> renderers) {
        this.renderers = renderers;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the behaviorRenderers
     */
    @XmlElement(name = "client-behavior-renderer", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(BehaviorRendererAdapter.class)
    public List<BehaviorRendererModel> getBehaviorRenderers() {
        return this.behaviorRenderers;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param behaviorRenderers the behaviorRenderers to set
     */
    public void setBehaviorRenderers(List<BehaviorRendererModel> behaviorRenderers) {
        this.behaviorRenderers = behaviorRenderers;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the extension
     */
    @XmlElement(name = "render-kit-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public RenderKitExtension getExtension() {
        return super.getExtension();
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param extension the extension to set
     */
    public void setExtension(RenderKitExtension extension) {
        super.setExtension(extension);
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @author asmirnov@exadel.com
     */
    public static class RenderKitExtension extends ConfigExtension {
    }
}
