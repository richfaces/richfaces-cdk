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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.ConfigExtension;
import org.richfaces.cdk.model.ConverterModel;
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.Extensible;
import org.richfaces.cdk.model.FunctionModel;
import org.richfaces.cdk.model.RenderKitModel;
import org.richfaces.cdk.model.Taglib;
import org.richfaces.cdk.model.ValidatorModel;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlRootElement(name = "faces-config", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
@XmlType(name = "faces-configType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
public class FacesConfigBean implements Extensible<FacesConfigBean.FacesConfigExtension> {
    @XmlElement(name = "component", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(ComponentAdapter.class)
    private List<ComponentModel> components = Lists.newArrayList();

    @XmlElement(name = "render-kit", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(RenderKitAdapter.class)
    private List<RenderKitModel> renderKits = Lists.newArrayList();

    @XmlElement(name = "converter", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(ConverterAdapter.class)
    private List<ConverterModel> converters = Lists.newArrayList();

    @XmlElement(name = "validator", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(ValidatorAdapter.class)
    private List<ValidatorModel> validators = Lists.newArrayList();

    @XmlElement(name = "behavior", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(BehaviorAdapter.class)
    private List<BehaviorModel> behaviors = Lists.newArrayList();

    @XmlAttribute
    private String version = "2.0";

    @XmlAttribute(name = "metadata-complete")
    private Boolean metadataComplete;

    @XmlElement(name = "faces-config-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    private FacesConfigExtension extension;

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the metadataComplete
     */
    public Boolean getMetadataComplete() {
        return this.metadataComplete;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param metadataComplete the metadataComplete to set
     */
    public void setMetadataComplete(Boolean metadataComplete) {
        this.metadataComplete = metadataComplete;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the components
     */
    public List<ComponentModel> getComponents() {
        return components;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param components the components to set
     */
    public void setComponents(List<ComponentModel> components) {
        this.components = components;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the renderKits
     */
    public List<RenderKitModel> getRenderKits() {
        return renderKits;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param renderKits the renderKits to set
     */
    public void setRenderKits(List<RenderKitModel> renderKits) {
        this.renderKits = renderKits;
    }

    public List<ConverterModel> getConverters() {
        return converters;
    }

    public void setConverters(List<ConverterModel> converters) {
        this.converters = converters;
    }

    public List<ValidatorModel> getValidators() {
        return validators;
    }

    public void setValidators(List<ValidatorModel> validators) {
        this.validators = validators;
    }

    public List<BehaviorModel> getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(List<BehaviorModel> behaviors) {
        this.behaviors = behaviors;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the extension
     */
    public FacesConfigExtension getExtension() {
        return extension;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param extension the extension to set
     */
    public void setExtension(FacesConfigExtension extension) {
        this.extension = extension;
    }

    public static final class FacesConfigExtension extends ConfigExtension {
        
        private String prefix;
        
        private Taglib taglib;

        private List<EventModel> events = Lists.newArrayList();
        
        private List<FunctionModel> functions = Lists.newArrayList();

        @XmlElement(name = "faces-event", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        @XmlJavaTypeAdapter(EventAdapter.class)
        public List<EventModel> getEvents() {
            return events;
        }

        /**
         * <p class="changed_added_4_0"></p>
         *
         * @param events the events to set
         */
        public void setEvents(List<EventModel> events) {
            this.events = events;
        }

        @XmlElement(name = "prefix", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }

        @XmlElement(name = "taglib", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public void setTaglib(Taglib taglib) {
            this.taglib = taglib;
        }

        public Taglib getTaglib() {
            return taglib;
        }

        /**
         * <p class="changed_added_4_0"></p>
         * @return the functions
         */
        @XmlElement(name = "function", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        @XmlJavaTypeAdapter(FunctionAdapter.class)
        public List<FunctionModel> getFunctions() {
            return this.functions;
        }

        /**
         * <p class="changed_added_4_0"></p>
         * @param functions the functions to set
         */
        public void setFunctions(List<FunctionModel> functions) {
            this.functions = functions;
        }
    }
}
