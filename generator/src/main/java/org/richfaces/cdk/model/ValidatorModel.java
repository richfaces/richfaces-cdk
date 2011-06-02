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
public class ValidatorModel extends ModelElementBase implements ModelElement<ValidatorModel> {
    private static final long serialVersionUID = -6097027070372673266L;

    public ValidatorModel() {
    }

    public ValidatorModel(FacesId validatorId) {
        setId(validatorId);
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        return visitor.visitValidator(this, data);
    }

    @Override
    public void merge(ValidatorModel other) {
        ComponentLibrary.merge(getAttributes(), other.getAttributes());
        ComponentLibrary.merge(this, other);
    }

    @Override
    public boolean same(ValidatorModel other) {
        return null != getId() && getId().equals(other.getId());
    }
}
