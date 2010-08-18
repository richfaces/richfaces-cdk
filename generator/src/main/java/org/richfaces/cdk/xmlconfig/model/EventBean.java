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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.TagModel;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlType(name = "faces-eventType", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
public class EventBean {
    private String description;
    private ClassName listenerInterface;
    private ClassName listenerWrapper;
    private ClassName sourceInterface;
    private ClassName type;
    private List<TagModel> tags = Lists.newArrayList();

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the type
     */
    @XmlElement(name = "event-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public ClassName getType() {
        return type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param type the type to set
     */
    public void setType(ClassName type) {
        this.type = type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the description
     */
    @XmlElement(name = "description", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public String getDescription() {
        return description;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the listenerInterface
     */
    @XmlElement(name = "listener-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getListenerInterface() {
        return listenerInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param listenerInterface the listenerInterface to set
     */
    public void setListenerInterface(ClassName listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the listenerWrapper
     */
    @XmlElement(name = "listener-wrapper-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public ClassName getListenerWrapper() {
        return this.listenerWrapper;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param listenerWrapper the listenerWrapper to set
     */
    public void setListenerWrapper(ClassName listenerWrapper) {
        this.listenerWrapper = listenerWrapper;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the sourceInterface
     */
    @XmlElement(name = "source-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getSourceInterface() {
        return sourceInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param sourceInterface the sourceInterface to set
     */
    public void setSourceInterface(ClassName sourceInterface) {
        this.sourceInterface = sourceInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the tags
     */
    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the tags
     */
    @XmlElement(name = "tag", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public List<TagModel> getTags() {
        return this.tags;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param tags the tags to set
     */
    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }
}
