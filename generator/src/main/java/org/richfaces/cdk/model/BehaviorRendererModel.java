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

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@SuppressWarnings("serial")
public class BehaviorRendererModel extends DescriptionGroupBase implements ModelElement<BehaviorRendererModel>,
        GeneratedFacesComponent {
    private ClassName baseClass;
    private ClassName targetClass;
    private FacesId id;
    /**
     * <p class="changed_added_4_0">
     * Is that bean property generate in the class or should be generated ?
     * </p>
     */
    private Boolean generate;

    public BehaviorRendererModel(FacesId type) {
        this.id = type;
    }

    public BehaviorRendererModel() {
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

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the id
     */
    public FacesId getId() {
        return id;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the targetClass
     */
    @Merge
    public ClassName getTargetClass() {
        return targetClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param targetClass the targetClass to set
     */
    public void setTargetClass(ClassName rendererClass) {
        this.targetClass = rendererClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the baseClass
     */
    @Merge
    public ClassName getBaseClass() {
        return this.baseClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param baseClass the baseClass to set
     */
    public void setBaseClass(ClassName baseClass) {
        this.baseClass = baseClass;
    }

    @Override
    public void merge(BehaviorRendererModel other) {
        ComponentLibrary.merge(this, other);
    }

    @Override
    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        return visitor.visitBehaviorRenderer(this, data);
    }

    @Override
    public boolean same(BehaviorRendererModel other) {
        if (null != getId() && null != other.getId()) {
            // Both types not null, compare them.
            return getId().equals(other.getId());
        }
        // one or both types are null, compare classes.
        if (null != getTargetClass() && getTargetClass().equals(other.getTargetClass())) {
            return true;
        }
        return false;
    }

    @Merge
    public Boolean getGenerate() {
        return generate;
    }

    public void setGenerate(Boolean generate) {
        this.generate = generate;
    }
}
