/**
 * <h1>Java annotations used by the CDK.</h1>
 * <h2>Package-level annotations</h2>
 * <p> {@link org.richfaces.cdk.annotations.TagLibrary} defines library-wide parameters</p>
 * <h2>Class-level annotations:</h2>
 * <ul>
 * <li>&#064;{@link org.richfaces.cdk.annotations.JsfComponent}("component.Type") or &#064;{@link javax.faces.component.FacesComponent}("component.Type").</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.JsfRenderer} Defines JSF {@link javax.faces.render.Renderer} or link renderer with component.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.JsfConverter} Defines JSF {@link javax.faces.convert.Converter}.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.JsfValidator} Defines JSF {@link javax.faces.validator.Validator}.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.JsfBehavior} Defines JSF {@link javax.faces.component.behavior.ClientBehavior}.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.JsfBehaviorRenderer} Defines JSF {@link javax.faces.render.ClientBehaviorRenderer} or links renderer with behavior.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Event} Defines JSF {@link javax.faces.event.FacesEvent} or links event with component that fires it.</li>
 * </ul>
 * <h2>Annotations used to refine class top level definitions</h2>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Test} tells CDK to generate unit test for component.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Description} optional IDE-related parameters.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Tag} VDL tag description.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.RendererSpecificComponent} defines renderer specific component for the family created from one base component</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Description} optional IDE-related parameters.<br /></li>
 * </ul>
 * <p> </p>
 * <h2>Attribute level annotations ( for getter or field level ):</h2>
 * <ul>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Attribute} Defines Faces attribute, used on for attribute getter method or field.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Facet} for facet getter. Also, used in {@link org.richfaces.cdk.annotations.JsfComponent} facets property.</li>
 * <li>&#064;{@link org.richfaces.cdk.annotations.Function},  Used for public static methods to define them as EL-functions.</li>
 * </ul>
 * <h2>Annotations used to refine method-level properties</h2>
 * <ul>
 * <li>&#064;{@link Signature} defines Java method signature for attributes that holds MethodExpression objects.</li>
 * <li>&#064;{@link Alias} defines alias for attribute.</li>
 * <li>&#064;{@link EventName}(value="click",defaultEvent=true) defines behavior event name for that attribute.</li>
 * <li>&#064;{@link Description} optional IDE-related description.</li>
 * </ul>
 * <p> </p>
 * <h3>Facet annotations.</h3>
 * <p>There are two methods to define component facet. At the class level, developer could use &#064;{@link org.richfaces.cdk.annotations.JsfComponent#facets()} property. It is also possible to define facet getter/setter methods for facet and mark one of them with &#064;{@link Facet} annotation.</p>
 *
 *
 */
package org.richfaces.cdk.annotations;

