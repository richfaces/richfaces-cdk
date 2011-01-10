/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.templatecompiler;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.As;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.MethodSignature;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.RenderKitModel;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.templatecompiler.model.Template;
import org.richfaces.cdk.xmlconfig.JAXB;
import org.richfaces.cdk.xmlconfig.JAXBBinding;
import org.xml.sax.InputSource;
import org.xml.sax.ext.EntityResolver2;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 * 
 */
@RunWith(CdkTestRunner.class)
public class RendererTemplateParserTest extends CdkTestBase {

    @Inject
    @As(JAXBBinding.class)
    private JAXB binding;

    @Inject
    private ComponentLibrary library;

    @Inject
    private RendererTemplateParser parser;

    @Mock
    private EntityResolver2 resolver;

    @Mock
    @Source(Sources.RENDERER_TEMPLATES)
    private FileManager templatesSource;

    private void assertNoDefaultValue(PropertyBase property) {
        assertNull(property.getDefaultValue());
    }

    private void assertNoEventNames(PropertyBase property) {
        Set<EventName> eventNames = property.getEventNames();
        assertNotNull(eventNames);
        assertTrue(eventNames.isEmpty());
    }

    private void assertNoSignature(PropertyBase property) {
        MethodSignature signature = property.getSignature();
        assertNull(signature);
    }

    private void assertNotRequired(PropertyBase property) {
        assertFalse(property.isRequired());
    }

    @Test
    public void basicComponentTest() throws Exception {
        Template template = parser.parseTemplate(getJavaFile("org/richfaces/cdk/templatecompiler/basic.template.xml"));
        assertNotNull(template);

        parser.mergeTemplateIntoModel(template, null);

        RenderKitModel renderKit = getRenderkitFromModel(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        RendererModel renderer = getFirstRendererFromRenderkit(renderKit);

        assertEquals(new ClassName("org.richfaces.renderkit.html.BasicRendererImpl"), renderer.getRendererClass());
        assertFalse(renderer.isRendersChildren());
    }

    /**
     * Checks that method signature satisfies the following declaration: <code>java.lang.String action()</code>
     * 
     * @param actionProperty
     */
    private void checkDummyComponentAction(PropertyBase actionProperty) {
        assertNotNull(actionProperty);
        assertNoEventNames(actionProperty);

        assertNotNull(actionProperty.getSignature());
        assertEquals(0, actionProperty.getSignature().getParameters().size());
        assertEquals(String.class.getName(), actionProperty.getSignature().getReturnType().toString());
    }

    /**
     * Checks that method signature satisfies the following declaration:
     * <code>void actionListener(javax.faces.event.ActionEvent)</code>
     * 
     * @param actionListenerProperty
     */
    private void checkDummyComponentActionListener(PropertyBase actionListenerProperty) {
        assertNotNull(actionListenerProperty);
        assertNoEventNames(actionListenerProperty);

        assertEquals(Lists.newArrayList(new ClassName(ActionEvent.class)), actionListenerProperty.getSignature().getParameters());
    }

    /**
     * Checks that method signature satisfies the following declaration:
     * <code>float coolMethod(int, java.lang.String, javax.faces.validator.Validator)</code>
     * 
     * @param coolMethodProperty
     */
    private void checkDummyComponentCoolMethod(PropertyBase coolMethodProperty) {
        assertNotNull(coolMethodProperty);
        assertNoEventNames(coolMethodProperty);

        ArrayList<ClassName> expectedSignature =
            Lists.newArrayList(new ClassName(int.class), new ClassName(String.class), new ClassName(Validator.class));

        assertEquals(expectedSignature, coolMethodProperty.getSignature().getParameters());
    }

    private void checkDummyComponentImportedAttribute(PropertyBase importedAttribute, Class<?> type) {
        assertNotNull(importedAttribute);
        assertNoEventNames(importedAttribute);
        assertNoSignature(importedAttribute);
        assertNoDefaultValue(importedAttribute);
        assertFalse(importedAttribute.isRequired());

        assertEquals(type.getName(), importedAttribute.getType().getName());
    }

    /**
     * @param integerAttribute
     */
    private void checkDummyComponentIntegerAttribute(PropertyBase integerAttribute) {
        assertNotNull(integerAttribute);
        assertNoEventNames(integerAttribute);
        assertNoSignature(integerAttribute);
        assertNotRequired(integerAttribute);

        assertEquals(new ClassName(Integer.class), integerAttribute.getType());
        assertEquals("-1", integerAttribute.getDefaultValue());
    }

    /**
     * Checks the following conditions for attribute:<br />
     * - signature is empty<br />
     * - There's a single "change" event name that is not a default event<br />
     * 
     * @param onchangeAttr
     */
    private void checkDummyComponentOnchange(PropertyBase onchangeAttr) {
        assertNotNull(onchangeAttr);
        assertNoSignature(onchangeAttr);

        Set<EventName> changeEvents = onchangeAttr.getEventNames();
        assertNotNull(changeEvents);

        EventName changeEventName;
        Iterator<EventName> changeEventsIterator = changeEvents.iterator();
        assertTrue(changeEventsIterator.hasNext());

        changeEventName = changeEventsIterator.next();
        assertEquals("change", changeEventName.getName());
        assertFalse(changeEventName.isDefaultEvent());

        assertFalse(changeEventsIterator.hasNext());
    }

    /**
     * Checks the following conditions for attribute:<br />
     * 
     * - signature is empty<br />
     * - First event name is "click" and it's not a default event<br />
     * - Second event name is "action" and it is a default event<br />
     * 
     * @param onclickAttr
     */
    private void checkDummyComponentOnclick(PropertyBase onclickAttr) {
        assertNotNull(onclickAttr);
        assertNoSignature(onclickAttr);

        Set<EventName> clickEvents = onclickAttr.getEventNames();
        assertNotNull(clickEvents);

        EventName clickEventName;
        Iterator<EventName> clickEventsIterator = clickEvents.iterator();
        assertTrue(clickEventsIterator.hasNext());

        clickEventName = clickEventsIterator.next();
        assertEquals("click", clickEventName.getName());
        assertFalse(clickEventName.isDefaultEvent());

        assertTrue(clickEventsIterator.hasNext());

        clickEventName = clickEventsIterator.next();
        assertEquals("action", clickEventName.getName());
        assertTrue(clickEventName.isDefaultEvent());

        assertFalse(clickEventsIterator.hasNext());
    }

    /**
     * @param requiredAttribute
     */
    private void checkDummyComponentRequiredAttribute(PropertyBase requiredAttribute) {
        assertNotNull(requiredAttribute);
        assertNoEventNames(requiredAttribute);
        assertNoSignature(requiredAttribute);
        assertNoDefaultValue(requiredAttribute);

        assertTrue(requiredAttribute.isRequired());
        assertEquals("That's a required attribute", requiredAttribute.getDescription());
        assertEquals("Required Attribute", requiredAttribute.getDisplayName());
    }

    @Test
    // TODO - fix import-attributes.
    public void dummyComponentTest() throws Exception {
        expect(resolver.getExternalSubset(EasyMock.<String> eq("cdk:root"), (String) anyObject())).andReturn(null);
        expect(
            resolver.resolveEntity((String) isNull(),
                eq("urn:resource:org/richfaces/cdk/templatecompiler/dummy-attributes.xml"))).andReturn(
                new InputSource(this.getClass().getResourceAsStream(
                    "/org/richfaces/cdk/templatecompiler/dummy-attributes.xml")));
        expect(resolver.getExternalSubset(EasyMock.<String> eq("cdk:properties"), (String) isNull())).andReturn(null);
        expect(resolver.resolveEntity((String) isNull(), eq("urn:attributes:dummy-template-props.xml"))).andReturn(
            new InputSource(this.getClass().getResourceAsStream("/META-INF/cdk/attributes/dummy-template-props.xml")));
        expect(resolver.getExternalSubset(EasyMock.<String> eq("cdk:properties"), (String) isNull())).andReturn(null);

        replay(resolver, templatesSource);
        Template template = parser.parseTemplate(getJavaFile("org/richfaces/cdk/templatecompiler/dummy.template.xml"));
        assertNotNull(template);

        parser.mergeTemplateIntoModel(template, null);
        verify(resolver, templatesSource);

        RenderKitModel renderKit = getRenderkitFromModel("org.richfaces.CUSTOM_RENDERKIT");
        RendererModel renderer = getFirstRendererFromRenderkit(renderKit);

        assertEquals(new ClassName("org.richfaces.renderkit.html.DummyRendererImpl"), renderer.getRendererClass());

        assertTrue(renderer.isRendersChildren());
        assertEquals(FacesId.parseId("org.richfaces.Dummy"), renderer.getFamily());
        assertSame(template, renderer.getTemplate());

        Collection<PropertyBase> attributes = renderer.getAttributes();
        assertNotNull(attributes);

        checkDummyComponentOnclick(getAttribute(attributes, "onclick"));
        checkDummyComponentOnchange(getAttribute(attributes, "onchange"));
        checkDummyComponentAction(getAttribute(attributes, "action"));
        checkDummyComponentActionListener(getAttribute(attributes, "actionListener"));
        checkDummyComponentCoolMethod(getAttribute(attributes, "coolMethod"));
        checkDummyComponentIntegerAttribute(getAttribute(attributes, "integerAttribute"));
        checkDummyComponentRequiredAttribute(getAttribute(attributes, "requiredAttribute"));

        checkDummyComponentImportedAttribute(getAttribute(attributes, "anotherImportedStringProperty"), String.class);
        checkDummyComponentImportedAttribute(getAttribute(attributes, "anotherImportedProperty"), Object.class);
        checkDummyComponentImportedAttribute(getAttribute(attributes, "importedBooleanProperty"), boolean.class);
        checkDummyComponentImportedAttribute(getAttribute(attributes, "importedIntegerProperty"), Integer.class);

        assertEquals(11, attributes.size());
    }

    private PropertyBase getAttribute(Collection<PropertyBase> attributes, String string) {
        for (PropertyBase property : attributes) {
            if (string.equals(property.getName())) {
                return property;
            }
        }
        return null;
    }

    private RendererModel getFirstRendererFromRenderkit(RenderKitModel renderKit) {
        Collection<RendererModel> renderers = renderKit.getRenderers();
        assertNotNull(renderers);

        Iterator<RendererModel> renderersIterator = renderers.iterator();
        assertTrue(renderersIterator.hasNext());

        RendererModel renderer = renderersIterator.next();
        assertNotNull(renderer);

        return renderer;
    }

    private RenderKitModel getRenderkitFromModel(String renderkitId) {
        RenderKitModel renderKit = library.getRenderKit(new FacesId(renderkitId));
        assertNotNull(renderKit);

        return renderKit;
    }

}
