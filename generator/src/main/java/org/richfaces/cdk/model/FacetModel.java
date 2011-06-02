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
public class FacetModel extends DescriptionGroupBase implements ModelElement<FacetModel>, Named {
    private static final long serialVersionUID = 7723771279129598243L;
    private String name;
    /**
     * <p class="changed_added_4_0">
     * Is that bean property generate in the class or should be generated ?
     * </p>
     */
    private Boolean generate = Boolean.FALSE; // TODO - check in model validator.

    public FacetModel() {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void merge(FacetModel other) {
        ComponentLibrary.merge(this, other);
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        return visitor.visitFacet(this, data);
    }

    @Override
    public boolean same(FacetModel other) {
        return ComparatorUtils.nullSafeEquals(this.getName(), other.getName());
    }

    @Merge
    public Boolean getGenerate() {
        return generate;
    }

    public void setGenerate(Boolean generate) {
        this.generate = generate;
    }
}
