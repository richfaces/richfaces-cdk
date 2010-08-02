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
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface JsfBehavior {

    public static final String NAME = "org.richfaces.cdk.annotations.JsfBehavior";

    /**
     * <p class="changed_added_4_0">
     * behavior-id with which instances of implementation class can be created b JSF Application implementation. If this
     * value an empty, behavior-id will be inferred from class name.
     * </p>
     * 
     * @return converter type.
     */
    public String id() default "";

    public String generate() default "";

    public Tag tag() default @Tag;
    
    public JsfBehaviorRenderer renderer() default @JsfBehaviorRenderer();
    /**
     * <p class="changed_added_4_0">
     * Description used by IDE.
     * </p>
     * 
     * @return
     */
    public Description description() default @Description();


    /**
     * <p class="changed_added_4_0">
     * defines fragments of faces-config.xml that contain standard attribute definitions. CDK also tries to read
     * META-INF/cdk/attributes/[classname].xml file for all component superclasses and interfaces, therefore it is not
     * necessary to explicit include definitions for UIComponent and any other standard JSF classes. CDK defines couple
     * of its own "urn" namespaces: "urn:resource:" for classpath resources, "urn:config:" for for project configuration
     * folder and "urn:attributes:" for META-INF/cdk/attributes/ in the annotations library.
     * </p>
     * 
     * @return
     */
    public String[] attributes() default {};
}
