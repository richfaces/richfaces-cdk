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

import java.util.List;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0">
 * That class contains properties collection from standard include file.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class Properties {
    private List<? extends PropertyModel> properties = Lists.newArrayList();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the properties
     */
    public List<? extends PropertyModel> getProperties() {
        return properties;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param properties the properties to set
     */
    public void setProperties(List<? extends PropertyModel> properties) {
        this.properties = properties;
    }
}
