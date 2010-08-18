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
 * Description used that would be used in IDE to display.
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
public @interface Description {

    public static final String NAME = "org.richfaces.cdk.annotations.Description";

    /**
     * <p class="changed_added_4_0">Long description of the object.</p>
     * @return
     */
    public String value() default "";
    /**
     * <p class="changed_added_4_0">
     * Display name for development tools.
     * </p>
     * 
     * @return
     */
    public String displayName() default "";

    /**
     * <p class="changed_added_4_0">
     * URL that defines IDE icon.
     * </p>
     * 
     * @return Description url.
     */
    public String smallIcon() default "";

    /**
     * <p class="changed_added_4_0"></p>
     * @return
     */
    public String largeIcon() default "";

}
