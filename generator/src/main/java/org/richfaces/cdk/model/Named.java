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

import com.google.common.base.Predicate;

/**
 * <p class="changed_added_4_0">
 * This interface represents all models for "named" JSF objects ( attributes, properties, facets )
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface Named {
    /**
     * <p class="changed_added_4_0">
     * Predicate used to lookup named element in the {@link ModelCollection}
     * </p>
     *
     * @author asmirnov@exadel.com
     *
     */
    public static final class NamePredicate implements Predicate<Named> {
        private final String name;

        public NamePredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean apply(Named input) {
            return ComparatorUtils.nullSafeEquals(input.getName(), this.name);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * // TODO - change to class corresponding with Java identifier type from faces-config.xsd
     *
     * @return the name
     */
    String getName();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param name the name to set
     */
    void setName(String name);
}
