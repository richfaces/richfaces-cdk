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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p class="changed_added_4_0">
 * This annotation defines static method or all public static methods in the annotated class as EL functions.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface Function {
    Description description() default @Description();

    /**
     * <p class="changed_added_4_0">
     * EL-function name
     * </p>
     *
     * @return
     */
    String name() default "";

    /**
     * <p class="changed_added_4_0">
     * Tag library for which given function will be included.
     * </p>
     *
     * @return
     */
    TagType type() default TagType.Facelets;
}
