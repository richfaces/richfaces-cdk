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

import javax.faces.event.FacesEvent;

/**
 * <p class="changed_added_4_0">
 * That annotation marks class as JSF component. The difference with JSF 2.0 &#064;
 * {@link javax.faces.component.FacesComponent} annotation is what this one could marks abstaract class from which a
 * real UI-component implementation will be generated. The value of default {@link #type()} attribute is taken to be
 * <em>component type</em>. The fully qualified class name becomes a component class unless that class is abstract or
 * final component class is defined by the {@link #generate()} attribute value.
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface JsfComponent {

    /**
     * <p class="changed_added_4_0">
     * Annotation class name to use as key for annotation processor class.
     * </p>
     */
    public static final String NAME = "org.richfaces.cdk.annotations.JsfComponent";

    /**
     * <p class="changed_added_4_0">
     * Excplicitly disable component generation
     * </p>
     */
    public static final String DISABLED = "##DISABLED";

    /**
     * <p class="changed_added_4_0">
     * Type of the component. This is mandatory parameter because CDK uses <em>component-type</em> as primary key for
     * components library model.
     * </p>
     * <p class="todo">
     * TODO if this value is an empty, component type will be inferred from class name.
     * </p>
     * 
     * @return component type.
     */
    public String type() default "";

    /**
     * <p class="changed_added_4_0">
     * Component famili. If this attribute was empty, it is inferred from the COMPONENT_FAMILY constant or by naming
     * conventions.
     * </p>
     * 
     * @return
     */
    public String family() default "";

    /**
     * <p class="changed_added_4_0">
     * Name of the generated component implementation class.
     * </p>
     * 
     * @return
     */
    public String generate() default "";

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
     * Cenerated Junit test.
     * </p>
     * 
     * @return
     */
    public Test test() default @Test(testClass = "");

    /**
     * <p class="changed_added_4_0">
     * JsfRenderer associated with this component.
     * </p>
     * 
     * @return
     */
    public JsfRenderer renderer() default @JsfRenderer();

    /**
     * <p class="changed_added_4_0">
     * View Description Language, JSP or Facelets, tags.
     * </p>
     * 
     * @return
     */
    public Tag[] tag() default { @Tag };

    /**
     * <p class="changed_added_4_0">
     * Events fired by the component.
     * </p>
     * 
     * @return
     */
    public Class<? extends FacesEvent>[] fires() default {};

    /**
     * <p class="changed_added_4_0">
     * Component facets.
     * </p>
     * 
     * @return
     */
    public Facet[] facets() default {};

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

    /**
     * <p class="changed_added_4_0">
     * Interfaces that should be implemented in the generated component class. CDK processes all {@link Attribute} and
     * {@link Facet} annotations in these interfaces
     * </p>
     * 
     * @return
     */
    public Class<?>[] interfaces() default {};
    
    /**
     * <p class="changed_added_4_0"></p>
     * @return
     */
    public RendererSpecificComponent[] components() default {};

}
