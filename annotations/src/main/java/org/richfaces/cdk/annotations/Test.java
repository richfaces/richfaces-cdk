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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p class="changed_added_4_0">
 * Mark component class or method for automated testing.
 * </p>
 * <p class="todo">
 * TODO: introduce additional parameters to refine generated test.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
public @interface Test {
    String NAME = "org.richfaces.cdk.annotations.Test";

    /**
     * <p class="changed_added_4_0">
     * Name of the generated unit test class. Currently that is mandatory
     * </p>
     * <p class="todo">
     * TODO: if this value is an empty, class will be inferred from the base class name.
     * </p>
     *
     * @return name of the generated test class.
     */
    String testClass() default "";

    /**
     * <p class="changed_added_4_0">
     * The value of this annotation attribute is taken to be a name of the generated test method.
     * </p>
     *
     * @return
     */
    String testMethod() default "";

    /**
     * <p class="changed_added_4_0">
     * The value of this annotation attribute tells CDK what kind of tests should be generated.
     * </p>
     *
     * @return
     */
    TestType type() default TestType.ALL;
}
