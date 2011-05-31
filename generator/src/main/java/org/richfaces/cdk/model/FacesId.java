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

import java.io.Serializable;

import org.richfaces.cdk.util.Strings;

/**
 * <p class="changed_added_4_0">
 * This is a FacesId class that should be used to find JSF elements in {@link ComponentLibrary}
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class FacesId implements Serializable, Comparable<FacesId> {
    private static final long serialVersionUID = -8199984910177820771L;
    private final String type;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param type
     */
    public FacesId(String type) {
        this.type = type;
    }

    public static FacesId parseId(String id) {
        return Strings.isEmpty(id) ? null : new FacesId(id);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((type == null) ? 0 : type.hashCode());

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        FacesId other = (FacesId) obj;

        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(FacesId o) {
        return type.compareTo(o.getType());
    }
}
