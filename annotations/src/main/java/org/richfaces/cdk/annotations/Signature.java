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
 * This annotation defines a Java method signature for attributes that hold EL {@code MethodExpression} values.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public @interface Signature {
    /**
     * <p class="changed_added_4_0">
     * Method return type. Default is {@code Object}
     * </p>
     *
     * @return
     */
    Class<?> returnType() default Void.class;

    /**
     * <p class="changed_added_4_0">
     * Method parameters. Default is no-argument method.
     * </p>
     *
     * @return
     */
    Class<?>[] parameters() default {};

    public static final class NONE {
    }
}
