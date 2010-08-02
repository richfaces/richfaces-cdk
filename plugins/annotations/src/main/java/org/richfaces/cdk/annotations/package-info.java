/**
 * <h1>Java annotations used by the CDK.</h1>
 * <h2>Class-level annotations:</h2>
 * <p>Mandatory:</p>
 * <p>&#064;{@link JsfComponent}("component.Type") or &#064;{@link javax.faces.component.FacesComponent}("component.Type").</p>
 * <p>Optional:</p>
 * <ul>
 * <li>&#064;{@link Family}("component.Family") defines component family used in the generated class.<br /></li>
 * <li>&#064;{@link Generate}("component.UIClass") tells CDK to generate concrete component class ( base UI... or renderer-specific ).</li>
 * <li>&#064;{@link Test}(testClass="component.Test",testMethod="testFoo",testType={@link TestType}.DECODE) tells CDK to generate unit test for that component.</li>
 * <li>&#064;{@link JsfRenderer}("retnderer.Type") , &#064;{@link RendererTemplate}("/renderer/template.xml") associate renderer with that component. The first one defines independently created renderer, while &#064;{@link RendererTemplate tells CDK to generate renderer class from that template. It is possible to define more than one template with component class using &#064;{@link RendererTemplates}({&#064;{@link RendererTemplate}("one.xml"),&#064;{@link RendererTemplate}("two.xml").<br /></li>
 * <li>&#064;{@link Attributes}({"base.xml","command.xml"}) defines fragments of faces-config.xml which contain standart attributes definitions. CDK also tries to read META-INF/cdk/attributes/[classname].xml file for all component superclasses and interfaces, therefore it is not necessary to explicit include definitions for UIComponent and any other standard JSF classes. CDK defines couple of its own "urn" namespaces: "urn:resource:" for classpath resources, "urn:config:" for for project configuration folder and "urn:attributes:" for META-INF/cdk/attributes/ in the annotations library.<br /></li>
 * <li>&#064;{@link Fires}({MyEvent.class}) defines event classes that this component could fire.</li>
 * <li>&#064;{@link Description} , &#064;{@link DisplayName} - optional IDE-related parameters.<br /></li>
 * </ul>
 * <p> </p>
 * <h2>Attribute level annotations ( for getter or setter level ):</h2>
 * <ul>
 * <li>&#064;{@link Attribute}(literal="false",hidden="false",readOnly="false",passTrough="true") markes attribute getter/setter.</li>
 * <li>&#064;{@link Signature}(returnType=boolean.class,parameters={Object.class,String.class}) defines Java method signature for attributes that holds MethodExpression objects.</li>
 * <li>&#064;{@link Generate} forces CDK to generate attribute getter/setter. Without that annotation CDK will generate implementation for abstract methods only.</li>
 * <li>&#064;{@link Alias}({"foo","bar"})</li>
 * <li>&#064;{@link EventName}(value="click",defaultEvent=true) defines behavior event name for that attribute. To define more than one event name they could be grouped as &#064;{@link EventNames}({&#064;{@link EventName}("foo),&#064;{@link EventName}("bar")})</li>
 * <li>&#064;{@link DefaultValue}("12"), &#064;{@link SuggestedValue}("12") ...</li>
 * <li>&#064;{@link Description} , &#064;{@link DisplayName} - optional IDE-related parameters.</li>
 * </ul>
 * <p> </p>
 * <h3>Facet annotations.</h3>
 * <p>There are two methods to define component facet. At the class level, developer could use &#064;{@link Facets annotations. It is also possible to define facet getter/setter methods for facet and mark one of them with &#064;{@link Facet} annotation.</p>
 *
 *
 */
package org.richfaces.cdk.annotations;

