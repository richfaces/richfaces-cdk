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
public class ListenerModel implements ModelElement<ListenerModel> {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private FacesId id;

    public ListenerModel(FacesId type) {
        this.id = type;
    }

    public ListenerModel() {
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        return visitor.visitListener(this, data);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.model.ModelElement#getType()
     */
    public FacesId getId() {
        return id;
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

    @Override
    public void merge(ListenerModel other) {

        // TODO Auto-generated method stub
    }

    @Override
    public boolean same(ListenerModel other) {
        return equals(other);
    }
}
