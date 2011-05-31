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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p class="changed_added_4_0">
 * Attribute aliases. CDK will generate getter and setter methods for these aliases which delegate calls to the original
 * attribute methods.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.CLASS)
public @interface Alias {
    String NAME = "org.richfaces.cdk.annotations.Alias";

    /**
     * <p class="changed_added_4_0">
     * Attribute alias. This is mandatory parameter
     * </p>
     *
     * @return attribute aliases.
     */
    String value();
}
