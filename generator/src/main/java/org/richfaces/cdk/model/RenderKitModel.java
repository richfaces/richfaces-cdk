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

import org.richfaces.cdk.util.ComparatorUtils;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class RenderKitModel extends DescriptionGroupBase implements ModelElement<RenderKitModel>, FacesComponent {
    private static final long serialVersionUID = -7387781530198813352L;
    private final ModelSet<RendererModel> renderers = ModelSet.<RendererModel>create();
    private final ModelSet<BehaviorRendererModel> behaviorRenderers = ModelSet.<BehaviorRendererModel>create();
    private ClassName targetClass;
    private FacesId id;

    public RenderKitModel() {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the id
     */
    public FacesId getId() {
        return this.id;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param id the id to set
     */
    public void setId(FacesId id) {
        this.id = id;
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
    public void setTargetClass(ClassName renderKitClass) {
        this.targetClass = renderKitClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the renderers
     */
    public ModelCollection<RendererModel> getRenderers() {
        return renderers;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the behaviorRenderers
     */
    public ModelCollection<BehaviorRendererModel> getBehaviorRenderers() {
        return behaviorRenderers;
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        R result = visitor.visitRenderKit(this, data);

        result = renderers.accept(result, visitor, data);
        result = behaviorRenderers.accept(result, visitor, data);
        return result;
    }

    @Override
    public void merge(RenderKitModel other) {
        ComponentLibrary.merge(renderers, other.renderers);
        ComponentLibrary.merge(behaviorRenderers, other.behaviorRenderers);

        ComponentLibrary.merge(this, other);
    }

    @Override
    public boolean same(RenderKitModel other) {
        if (null != getId() && null != other.getId()) {
            return getId().equals(other.getId());
        } else {
            return ComparatorUtils.nullSafeEquals(getTargetClass(), other.getTargetClass());
        }
    }
}
