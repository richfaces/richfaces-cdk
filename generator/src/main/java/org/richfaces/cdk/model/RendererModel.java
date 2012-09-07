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

import org.richfaces.cdk.templatecompiler.model.Template;
import org.richfaces.cdk.util.Strings;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class RendererModel extends ModelElementBase implements ModelElement<RendererModel>, Cacheable {
    private static final long serialVersionUID = -5802466539382148578L;
    private FacesId family;
    private String componentType;
    private String templatePath;
    private Template template;
    private boolean rendersChildren;

    private boolean changed = true;
    private boolean changeTracking = true;

    public RendererModel() {
    }

    public RendererModel(FacesId type) {
        setId(type);
    }

    @Merge
    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    @Merge
    public FacesId getFamily() {
        return family;
    }

    public void setFamily(FacesId family) {
        this.family = family;
    }

    public ClassName getRendererClass() {
        return getTargetClass();
    }

    public void setRendererClass(ClassName rendererClass) {
        setTargetClass(rendererClass);
    }

    public boolean isRendersChildren() {
        return this.rendersChildren;
    }

    public void setRendersChildren(boolean rendersChildren) {
        this.rendersChildren = rendersChildren;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        return visitor.visitRender(this, data);
    }

    @Override
    public void merge(RendererModel other) {
        if (other == null) {
            return;
        }

        if (this.changeTracking) {
            this.changed = true;
        }

        ComponentLibrary.merge(this, other);

        // TODO review
        ClassName targetClass = this.getTargetClass();
        if (targetClass == null || Strings.isEmpty(targetClass.getName())) {
            setTargetClass(other.getTargetClass());
        }

        ClassName baseClass = this.getBaseClass();
        if (baseClass == null || Strings.isEmpty(baseClass.getName())) {
            setTargetClass(other.getBaseClass());
        }
    }

    @Override
    public boolean same(RendererModel other) {
        if (null != getId() && null != other.getId()) {
            // compare families ?
            if (null != getFamily() && null != other.getFamily() && !getFamily().equals(other.getFamily())) {
                return false;
            }
            // Both types not null, compare them.
            return getId().equals(other.getId());
        }
        // one or both types are null, compare classes.
        if (null != getRendererClass() && getRendererClass().equals(other.getRendererClass())) {
            return true;
        }
        return false;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    @Override
    protected PropertyBase createAttribute() {
        return new AttributeModel();
    }

    @Override
    public String toString() {
        return "Renderer {type: " + getId() + ", family: " + getFamily() + "}";
    }

    @Override
    public void markUnchanged() {
        this.changed = false;
    }

    @Override
    public boolean hasChanged() {
        return this.changed;
    }

    @Override
    public void stopTrackingChanges() {
        this.changeTracking = false;
    }
}
