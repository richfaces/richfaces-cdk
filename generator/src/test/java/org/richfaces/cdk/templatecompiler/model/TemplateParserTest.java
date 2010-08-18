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

package org.richfaces.cdk.templatecompiler.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.xmlconfig.JaxbTestBase;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class TemplateParserTest extends JaxbTestBase {

    public static final String TEMPLATE_PROLOG =
        "<cdk:root xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:cdk=\"http://richfaces.org/cdk/core\" xmlns:c=\"http://richfaces.org/cdk/jstl/core\" xmlns:cc=\"http://richfaces.org/cdk/jsf/composite\"><cc:interface>";
    private static final Object DEFAULT_ATTRIBUTE_TYPE = new ClassName(Object.class);
    private static final String TEMPLATE_EPILOG = "</cc:implementation></cdk:root>";
    private static final String TEMPLATE_MIDDLE = "</cc:interface><cc:implementation>";

    @Test
    public void testAttributes() throws Exception {
        Template template =
            unmarshal(
                Template.class,
                TEMPLATE_PROLOG
                    + "<cdk:renders-children>true</cdk:renders-children>"
                    + "<cc:attribute name=\"onclick\" />"
                    + "<cc:attribute name=\"mode\" default=\"ajax\" />"
                    + "<cc:attribute name=\"action\" method-signature=\"void action()\" />"
                    + "<cc:attribute name=\"changeListener\" method-signature=\"void changeListener(ValueChangeEvent)\" targets=\"changes changes1\" />"
                    + "<cc:attribute name=\"disabled\" type=\"boolean\" />"
                    + "<cc:attribute name=\"delay\" type=\"java.lang.Integer\" />"
                    + "<cc:attribute name=\"id\" required=\"true\" />"
                    + "<cc:attribute name=\"experts\" shortDescription=\"For use by experts\" displayName=\"Expert attribute\" expert=\"true\" />"
                    + "<cc:attribute name=\"preferred\" shortDescription=\"It's a preferred attribute\" displayName=\"Preferred attribute\" preferred=\"true\" />"
                    + "<cc:attribute name=\"onchange\">" + "<cc:clientBehavior event=\"change\" />"
                    + "<cc:clientBehavior event=\"valueChange\" default=\"true\" />" + "</cc:attribute>"
                    + TEMPLATE_MIDDLE + TEMPLATE_EPILOG);

        CompositeInterface interfaceSection = template.getInterface();
        assertNotNull(interfaceSection);

        List<Attribute> attributes = interfaceSection.getAttributes();
        assertNotNull(attributes);
        assertEquals(10, attributes.size());

        Attribute attribute;

        attribute = attributes.get(0);
        assertNotNull(attribute);
        assertEquals("onclick", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertNull(attribute.getMethodSignature());
        assertEquals(DEFAULT_ATTRIBUTE_TYPE, attribute.getType());
        assertNull(attribute.getTargets());
        assertFalse(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(1);
        assertNotNull(attribute);
        assertEquals("mode", attribute.getName());
        assertEquals("ajax", attribute.getDefaultValue());
        assertNull(attribute.getMethodSignature());
        assertEquals(DEFAULT_ATTRIBUTE_TYPE, attribute.getType());
        assertNull(attribute.getTargets());
        assertFalse(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(2);
        assertNotNull(attribute);
        assertEquals("action", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertEquals("void action()", attribute.getMethodSignature());
        assertEquals(DEFAULT_ATTRIBUTE_TYPE, attribute.getType());
        assertNull(attribute.getTargets());
        assertFalse(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(3);
        assertNotNull(attribute);
        assertEquals("changeListener", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertEquals("void changeListener(ValueChangeEvent)", attribute.getMethodSignature());
        assertEquals(DEFAULT_ATTRIBUTE_TYPE, attribute.getType());
        assertEquals("changes changes1", attribute.getTargets());
        assertFalse(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(4);
        assertNotNull(attribute);
        assertEquals("disabled", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertNull(attribute.getMethodSignature());
        assertEquals(ClassName.parseName("boolean"), attribute.getType());
        assertNull(attribute.getTargets());
        assertFalse(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(5);
        assertNotNull(attribute);
        assertEquals("delay", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertNull(attribute.getMethodSignature());
        assertEquals(ClassName.parseName("java.lang.Integer"), attribute.getType());
        assertNull(attribute.getTargets());
        assertFalse(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(6);
        assertNotNull(attribute);
        assertEquals("id", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertNull(attribute.getMethodSignature());
        assertEquals(DEFAULT_ATTRIBUTE_TYPE, attribute.getType());
        assertNull(attribute.getTargets());
        assertTrue(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(7);
        assertNotNull(attribute);
        assertFalse(attribute.isRequired());
        assertEquals("experts", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertNull(attribute.getMethodSignature());
        assertEquals(DEFAULT_ATTRIBUTE_TYPE, attribute.getType());
        assertNull(attribute.getTargets());
        assertEquals("For use by experts", attribute.getShortDescription());
        assertEquals("Expert attribute", attribute.getDisplayName());
        assertTrue(attribute.isExpert());
        assertFalse(attribute.isPreferred());

        attribute = attributes.get(8);
        assertNotNull(attribute);
        assertFalse(attribute.isRequired());
        assertFalse(attribute.isExpert());
        assertEquals("preferred", attribute.getName());
        assertNull(attribute.getDefaultValue());
        assertNull(attribute.getMethodSignature());
        assertEquals(DEFAULT_ATTRIBUTE_TYPE, attribute.getType());
        assertEquals("It's a preferred attribute", attribute.getShortDescription());
        assertEquals("Preferred attribute", attribute.getDisplayName());
        assertTrue(attribute.isPreferred());

        attribute = attributes.get(9);
        assertNotNull(attribute);
        assertEquals("onchange", attribute.getName());

        List<ClientBehavior> clientBehaviors = attribute.getClientBehaviors();
        assertNotNull(clientBehaviors);
        assertEquals(2, clientBehaviors.size());

        ClientBehavior clientBehavior;

        clientBehavior = clientBehaviors.get(0);
        assertNotNull(clientBehavior);
        assertEquals("change", clientBehavior.getEvent());
        assertFalse(clientBehavior.isDefaultEvent());

        clientBehavior = clientBehaviors.get(1);
        assertNotNull(clientBehavior);
        assertEquals("valueChange", clientBehavior.getEvent());
        assertTrue(clientBehavior.isDefaultEvent());

        assertEquals(Boolean.TRUE, interfaceSection.getRendersChildren());
    }

    @Test
    public void testImpl() throws Exception {
        Template template =
            unmarshal(
                Template.class,
                TEMPLATE_PROLOG
                    + TEMPLATE_MIDDLE
                    + "<cdk:call expression=\"#{cc.clientId}\"/><table width=\"200\"><tbody><cdk:call expression=\"#{cc.fooMethod(clientId)}\"/></tbody></table>Header<div class='bar'>foo</div>"
                    + TEMPLATE_EPILOG);

        CompositeImplementation implementation = template.getImplementation();
        assertNotNull(implementation);
        List<Object> children = implementation.getChildren();
        assertNotNull(children);
        assertEquals(4, children.size());
        assertEquals(CdkCallElement.class, children.get(0).getClass());
        assertEquals(AnyElement.class, children.get(1).getClass());
        assertEquals(String.class, children.get(2).getClass());
    }

    @Test
    public void testInterface() throws Exception {
        Template template =
            unmarshal(Template.class, TEMPLATE_PROLOG
                + "<cdk:class>org.richfaces.renderkit.html.TreeRenderer</cdk:class>"
                + "<cdk:superclass>org.richfaces.renderkit.TreeRendererBase</cdk:superclass>"
                + "<cdk:component-family>org.richfaces.TreeFamily</cdk:component-family>"
                + "<cdk:renderer-type>org.richfaces.TreeRenderer</cdk:renderer-type>"
                + "<cdk:renderkit-id>RF4_XHTML</cdk:renderkit-id>"
                + "<cdk:renders-children>false</cdk:renders-children>" + TEMPLATE_MIDDLE + TEMPLATE_EPILOG);

        CompositeInterface interfaceSection = template.getInterface();
        assertNotNull(interfaceSection);

        assertEquals(ClassName.parseName("org.richfaces.renderkit.html.TreeRenderer"), interfaceSection.getJavaClass());
        assertEquals(ClassName.parseName("org.richfaces.renderkit.TreeRendererBase"), interfaceSection.getBaseClass());
        assertEquals(FacesId.parseId("org.richfaces.TreeFamily"), interfaceSection.getComponentFamily());
        assertEquals(FacesId.parseId("org.richfaces.TreeRenderer"), interfaceSection.getRendererType());
        assertEquals("RF4_XHTML", interfaceSection.getRenderKitId());
        assertEquals(Boolean.FALSE, interfaceSection.getRendersChildren());

    }

    @Test
    public void testJstlCoreElements() throws Exception {
        Template template =
            unmarshal(Template.class, TEMPLATE_PROLOG + TEMPLATE_MIDDLE + "start"
                + "<c:if test=\"#{someTest}\">if content</c:if>" + "<c:choose>"
                + "<c:when test=\"#{anotherTest}\">when content</c:when>" + "<c:when test=\"#{coolTest}\">"
                + "<c:if test=\"#{nestedIfTest}\">nested if content</c:if>" + "</c:when>"
                + "<c:otherwise>otherwise content</c:otherwise>" + "</c:choose>"
                + "<c:forEach items=\"#{someCollection}\" var=\"iterationVar\">" + "forEach content" + "</c:forEach>"
                + "finish" + TEMPLATE_EPILOG);

        CompositeImplementation implementation = template.getImplementation();
        assertNotNull(implementation);

        List<Object> children = implementation.getChildren();
        assertNotNull(children);
        assertEquals(5, children.size());
        assertEquals("start", children.get(0));
        assertEquals(CdkIfElement.class, children.get(1).getClass());
        CdkIfElement ifElement = (CdkIfElement) children.get(1);
        assertEquals("#{someTest}", ifElement.getTest());
        List<Object> ifChildren = ifElement.getChildren();
        assertNotNull(ifChildren);
        assertEquals(1, ifChildren.size());
        assertEquals("if content", ifChildren.get(0));

        assertEquals(CdkChooseElement.class, children.get(2).getClass());
        CdkChooseElement chooseElement = (CdkChooseElement) children.get(2);
        List<Object> chooseChildren = chooseElement.getChildren();
        assertNotNull(chooseChildren);
        assertEquals(3, chooseChildren.size());

        assertEquals(CdkWhenElement.class, chooseChildren.get(0).getClass());
        CdkWhenElement firstWhen = (CdkWhenElement) chooseChildren.get(0);
        assertEquals("#{anotherTest}", firstWhen.getTest());
        List<Object> childrenOfFirstWhen = firstWhen.getChildren();
        assertNotNull(childrenOfFirstWhen);
        assertEquals(1, childrenOfFirstWhen.size());
        assertEquals("when content", childrenOfFirstWhen.get(0));

        assertEquals(CdkWhenElement.class, chooseChildren.get(1).getClass());
        CdkWhenElement secondWhen = (CdkWhenElement) chooseChildren.get(1);
        assertEquals("#{coolTest}", secondWhen.getTest());

        List<Object> childrenOfSecondWhen = secondWhen.getChildren();
        assertNotNull(childrenOfSecondWhen);
        assertEquals(1, childrenOfSecondWhen.size());

        assertEquals(CdkIfElement.class, childrenOfSecondWhen.get(0).getClass());
        CdkIfElement nestedIf = (CdkIfElement) childrenOfSecondWhen.get(0);
        assertEquals("#{nestedIfTest}", nestedIf.getTest());
        List<Object> childrenOfNestedIf = nestedIf.getChildren();
        assertNotNull(childrenOfNestedIf);
        assertEquals(1, childrenOfNestedIf.size());
        assertEquals("nested if content", childrenOfNestedIf.get(0));

        assertEquals(CdkOtherwiseElement.class, chooseChildren.get(2).getClass());
        CdkOtherwiseElement otherwiseElement = (CdkOtherwiseElement) chooseChildren.get(2);
        List<Object> childrenOfOtherwiseElement = otherwiseElement.getChildren();
        assertNotNull(childrenOfOtherwiseElement);
        assertEquals(1, childrenOfOtherwiseElement.size());
        assertEquals("otherwise content", childrenOfOtherwiseElement.get(0));

        assertEquals(CdkForEachElement.class, children.get(3).getClass());
        CdkForEachElement forEachElement = (CdkForEachElement) children.get(3);
        assertEquals("#{someCollection}", forEachElement.getItems());
        assertEquals("iterationVar", forEachElement.getVar());

        List<Object> forEachChildren = forEachElement.getChildren();
        assertNotNull(forEachChildren);
        assertEquals(1, forEachChildren.size());
        assertEquals("forEach content", forEachChildren.get(0));

        assertEquals("finish", children.get(4));
    }

    @Test
    public void testObject() throws Exception {
        Template template =
            unmarshal(Template.class, TEMPLATE_PROLOG + TEMPLATE_MIDDLE
                + "<cdk:object name=\"rowCount\" type=\"int\" value=\"#{getRowCount(context)}\" />"
                + "<cdk:object name=\"bodyExpression\" type=\"java.lang.String\">"
                + "\"test expression\" +\n\"second line\"" + "</cdk:object>" +

                TEMPLATE_EPILOG);

        CompositeImplementation implementation = template.getImplementation();
        assertNotNull(implementation);

        List<Object> children = implementation.getChildren();
        assertNotNull(children);
        assertEquals(2, children.size());

        assertEquals(CdkObjectElement.class, children.get(0).getClass());
        CdkObjectElement firstObject = (CdkObjectElement) children.get(0);
        assertNotNull(firstObject);
        assertEquals("rowCount", firstObject.getName());
        assertEquals("int", firstObject.getType());
        assertEquals("#{getRowCount(context)}", firstObject.getValue());

        assertEquals(CdkObjectElement.class, children.get(1).getClass());
        CdkObjectElement secondObject = (CdkObjectElement) children.get(1);
        assertNotNull(secondObject);
        assertEquals("bodyExpression", secondObject.getName());
        assertEquals("java.lang.String", secondObject.getType());
        assertEquals("\"test expression\" +\n\"second line\"", secondObject.getBodyValue());
    }

    @Test
    public void testResourceDependencies() throws Exception {
        Template template =
            unmarshal(Template.class, TEMPLATE_PROLOG + "<cdk:resource-dependency name=\"jquery.js\" />"
                + "<cdk:resource-dependency name=\"richfaces.css\" library=\"org.richfaces\" />"
                + "<cdk:resource-dependency name=\"richfaces.js\" library=\"org.richfaces\" target=\"body\" /> "

                + TEMPLATE_MIDDLE + TEMPLATE_EPILOG);

        CompositeInterface interfaceSection = template.getInterface();
        assertNotNull(interfaceSection);

        assertNull(interfaceSection.getRendersChildren());

        List<ResourceDependency> resourceDependencies = interfaceSection.getResourceDependencies();
        assertNotNull(resourceDependencies);
        assertEquals(3, resourceDependencies.size());

        ResourceDependency resourceDependency;

        resourceDependency = resourceDependencies.get(0);
        assertNotNull(resourceDependency);
        assertEquals("jquery.js", resourceDependency.getName());
        assertEquals("",resourceDependency.getLibrary());
        assertEquals("", resourceDependency.getTarget());

        resourceDependency = resourceDependencies.get(1);
        assertNotNull(resourceDependency);
        assertEquals("richfaces.css", resourceDependency.getName());
        assertEquals("org.richfaces", resourceDependency.getLibrary());
        assertEquals("", resourceDependency.getTarget());

        resourceDependency = resourceDependencies.get(2);
        assertNotNull(resourceDependency);
        assertEquals("richfaces.js", resourceDependency.getName());
        assertEquals("org.richfaces", resourceDependency.getLibrary());
        assertEquals("body", resourceDependency.getTarget());
    }

    @Test
    public void testTemplate() throws Exception {
        Template template = unmarshal(Template.class, TEMPLATE_PROLOG + TEMPLATE_MIDDLE + TEMPLATE_EPILOG);
        assertNotNull(template.getInterface());
        assertNotNull(template.getImplementation());
    }
}
