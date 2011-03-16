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
 * That annotation marks class as JSF component. The difference with JSF 2.0 &#064;
 * {@link javax.faces.component.FacesComponent} annotation is what this one could marks abstract class from which a
 * real UI-component implementation will be generated. 
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface JsfComponent {

    /**
     * <p class="changed_added_4_0">
     * Annotation class name to use as key in the annotation processor.
     * </p>
     */
    public static final String NAME = "org.richfaces.cdk.annotations.JsfComponent";


    /**
     * <p class="changed_added_4_0">
     * Type of the JSF component. 
     * </p>
     * <p class="naming">
     * if this value is an empty, component type would be inferred from class name.
     * </p>
     * 
     * @return component type.
     */
    public String type() default "";

    /**
     * <p class="changed_added_4_0">
     * Component family. For default value, it could be got from the COMPONENT_FAMILY constant or by inferred naming
     * conventions.
     * </p>
     * 
     * @return
     */
    public String family() default "";

    /**
     * <p class="changed_added_4_0">
     * Name of the generated component implementation class. Default value means nothing to genrate from concrete class,
     * or infer name by convention for abstract class.
     * </p>
     * 
     * @return
     */
    public String generate() default "";

    /**
     * <p class="changed_added_4_0">
     * Component description to include into generated faces-config and taglib.
     * </p>
     * 
     * @return
     */
    public Description description() default @Description();

    /**
     * <p class="changed_added_4_0">
     * Junit test description. Isn't used in RichFaces 4.0, reserved for future releases. 
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
     * Tag description. If generated tags require special handlers, provide separate description for every type of tag, JSP and Facelets.
     * Otherwise, the only one tag tag description with name and type {@link TagType#All}. 
     * </p>
     * 
     * @return
     */
    public Tag[] tag() default {};

    /**
     * <p class="changed_added_4_0">
     * @{link FacesEvent}s fired by the component.
     * </p>
     * 
     * @return
     */
    public Event[] fires() default {};

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
     * Defines file names for fragment of faces-config.xml that contain standard attribute definitions. All names relative to the
     * META-INF/cdk/attributes/ folder in classpath. CDK also tries to read
     * META-INF/cdk/attributes/[classname].xml file for all component superclasses and interfaces. Therefore, it is not
     * necessary to explicitly include definitions for UIComponent and any other standard JSF classes.
     * </p>
     * 
     * @return
     */
    public String[] attributes() default {};

    /**
     * <p class="changed_added_4_0">
     * Interfaces that should be implemented by the generated component class. CDK processes all {@link Attribute} and
     * {@link Facet} annotations in these interfaces
     * </p>
     * 
     * @return
     */
    public Class<?>[] interfaces() default {};
    
    /**
     * <p class="changed_added_4_0">Defines third-level renderer specific components. Used to generate a whole family of similar components.
     * For example, {@link javax.faces.component.UIComponentBase} provides {@link javax.faces.component.UICommand} subclass for all command components, and {@link javax.faces.component.html.HtmlCommandLink} with {@link javax.faces.component.HtmlCommandButton} are
     * renderer-specific components for links and buttons.</p>
     * @return
     */
    public RendererSpecificComponent[] components() default {};

}
