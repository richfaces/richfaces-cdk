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
package org.richfaces.cdk.annotations;

/**
 * <p class="changed_added_4_0">
 * Object or attribute description. Included into generated faces-config.xml or tag library, so it can be used by IDE.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public @interface Description {
    String NAME = "org.richfaces.cdk.annotations.Description";

    /**
     * <p class="changed_added_4_0">
     * Long description of the object. If omited, javadoc comment for associated Java element.
     * </p>
     *
     * @return
     */
    String value() default "";

    /**
     * <p class="changed_added_4_0">
     * Display name for development tools.
     * </p>
     *
     * @return
     */
    String displayName() default "";

    /**
     * <p class="changed_added_4_0">
     * URL that defines small IDE icon.
     * </p>
     *
     * @return Description url.
     */
    String smallIcon() default "";

    /**
     * <p class="changed_added_4_0">
     * URL that defines large IDE icon.
     * </p>
     *
     * @return
     */
    String largeIcon() default "";
}
