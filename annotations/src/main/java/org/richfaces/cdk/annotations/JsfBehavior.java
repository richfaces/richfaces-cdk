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

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.convert.Converter;

/**
 * <p class="changed_added_4_0">
 * This annotation defines concrete class as JSF {@link ClientBehavior}, or abstract class as the base for generated
 * Behavior implementation.
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

    /**
     * <p class="changed_added_4_0">
     * fully qualified class name of the generated Behavior implementation. Default value means nothing to genrate from concrete class,
     * or infer name by convention for abstract class.</p>
     * </p>
     * @return
     */
    public String generate() default "";

    /**
     * <p class="changed_added_4_0">
     * Tag description. If generated tags require special handlers, provide separate description for every type of tag, JSP and Facelets.
     * Otherwise, the only one tag tag description with name and type {@link TagType#All}. 
     * </p>
     * @return
     */
    public Tag[] tag() default {};
    
    /**
     * <p class="changed_added_4_0">Renderer associated with generated {@link ClientBehavior}</p>
     * @return
     */
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
     * Defines file names for fragment of faces-config.xml that contain standard attribute definitions. All names relative to the
     * META-INF/cdk/attributes/ folder in classpath. CDK also tries to read
     * META-INF/cdk/attributes/[classname].xml file for all component superclasses and interfaces. Therefore, it is not
     * necessary to explicitly include definitions for Converter and any other standard JSF classes.
     * </p>
     * 
     * @return
     */
    public String[] attributes() default {};

    /**
     * <p class="changed_added_4_0">
     * Interfaces that should be implemented in the generated class. CDK processes all {@link Attribute} annotations in these interfaces
     * </p>
     * 
     * @return
     */
    public Class<?>[] interfaces() default {};
}
