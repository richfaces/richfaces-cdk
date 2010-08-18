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



package org.richfaces.cdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.richfaces.cdk.util.ComparatorUtils;

/**
 * <p class="changed_added_4_0">That bean represent {@link javax.faces.event.FacesEvent} subclass that can be fired by
 * component.</p>
 *
 * @author asmirnov@exadel.com
 *
 */
@SuppressWarnings("serial")
@XmlType(name = "event-configType", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class EventModel implements ModelElement<EventModel> {
    private String description;
    private ClassName listenerInterface;
    private String  listenerMethod;
    private boolean generateListener;
    private ClassName sourceInterface;
    private boolean generateSource;
    private final ModelCollection<TagModel> tags = ModelSet.<TagModel>create();

    /**
     *  <p class="changed_added_4_0"></p>
     */
    private ClassName type;


    /**
     * <p class="changed_added_4_0"></p>
     * @return the description
     */
    @Merge
    public String getDescription() {
        return description;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the type
     */
    public ClassName getType() {
        return this.type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param type the type to set
     */
    public void setType(ClassName type) {
        this.type = type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the listenerInterface
     */
    @Merge
    public ClassName getListenerInterface() {
        return listenerInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param listenerInterface the listenerInterface to set
     */
    public void setListenerInterface(ClassName listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the listenerMethod
     */
    @Merge
    public String getListenerMethod() {
        return this.listenerMethod;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param listenerMethod the listenerMethod to set
     */
    public void setListenerMethod(String listenerMethodName) {
        this.listenerMethod = listenerMethodName;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the generateListener
     */
    @Merge
    public boolean isGenerateListener() {
        return this.generateListener;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param generateListener the generateListener to set
     */
    public void setGenerateListener(boolean generateListener) {
        this.generateListener = generateListener;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the sourceInterface
     */
    @Merge
    public ClassName getSourceInterface() {
        return sourceInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param sourceInterface the sourceInterface to set
     */
    public void setSourceInterface(ClassName sourceInterface) {
        this.sourceInterface = sourceInterface;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the generateSource
     */
    @Merge
    public boolean isGenerateSource() {
        return this.generateSource;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param generateSource the generateSource to set
     */
    public void setGenerateSource(boolean generateSource) {
        this.generateSource = generateSource;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the tags
     */
    @Merge
    public ModelCollection<TagModel> getTags() {
        return this.tags;
    }

    @Override
    public void merge(EventModel other) {
        ComponentLibrary.merge(this, other);
    }

    public <R,D> R accept(Visitor<R,D> visitor, D data) {
        return visitor.visitEvent(this,data);
    }

    @Override
    public boolean same(EventModel other) {
        return ComparatorUtils.nullSafeEquals(getType(), other.getType());
    }

}
