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
package org.richfaces.cdk.generate.java;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.el.MethodExpression;
import javax.faces.component.UIOutput;
import javax.faces.el.MethodBinding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.JavaSourceParser;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.MethodSignature;
import org.richfaces.cdk.model.PropertyBase;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(CdkTestRunner.class)
public class ComponentClassGeneratorTest extends AbstractClassGeneratorTest {
    @Inject
    private ComponentClassGenerator generator;

    public static ComponentModel createComponent() {
        ComponentModel component = new ComponentModel(FacesId.parseId("foo.bar"));
        component.setGenerate(true);
        component.setTargetClass(ClassName.parseName("org.richfaces.cdk.generate.java.GeneratedComponent"));
        component.setBaseClass(ClassName.parseName(UIOutput.class.getName()));
        component.setRendererType(FacesId.parseId("foo.barRenderer"));

        PropertyBase attribute = component.getOrCreateAttribute("testValue");
        attribute.setType(new ClassName(Object.class));
        attribute.setGenerate(true);

        attribute = component.getOrCreateAttribute("testFlag");
        attribute.setType(new ClassName(Boolean.TYPE));
        attribute.setRequired(true);
        attribute.setGenerate(true);

        attribute = component.getOrCreateAttribute("testBinding");
        attribute.setType(new ClassName(MethodBinding.class));
        attribute.setGenerate(true);
        attribute.setBinding(true);
        attribute.setBindingAttribute(true);

        attribute = component.getOrCreateAttribute("testExpr");
        attribute.setType(new ClassName(MethodExpression.class));
        attribute.setGenerate(true);
        attribute.setBindingAttribute(true);
        MethodSignature signature = new MethodSignature();
        signature.setParameters(Arrays.asList(new ClassName(String.class), new ClassName(Integer.class)));
        attribute.setSignature(signature);

        attribute = component.getOrCreateAttribute("id");
        attribute.setType(new ClassName(String.class));
        attribute.setGenerate(false);

        attribute = component.getOrCreateAttribute("listStrings");
        attribute.setType(new ClassName(new ArrayList<String>().getClass()));
        attribute.setGenerate(true);

        attribute = component.getOrCreateAttribute("listInteger");
        attribute.setType(new ClassName(new ArrayList<Integer>().getClass()));
        attribute.setGenerate(true);

        attribute = component.getOrCreateAttribute("list");
        attribute.setType(new ClassName(ArrayList.class));
        attribute.setGenerate(true);

        Set<EventName> eventNames = attribute.getEventNames();
        eventNames.add(getEvent("id", false));
        eventNames.add(getEvent("action", true));

        return component;
    }

    public ComponentClassGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(ComponentClassGenerator generator) {
        this.generator = generator;
    }

    @Test
    public void testGetOutputFileComponent() throws Exception {
        final StringWriter outputWriter = new StringWriter();
        expect(output.createOutput((String) anyObject(), anyInt())).andReturn(outputWriter);
        replay(output);

        ComponentModel component = createComponent();

        library.getComponents().add(component);

        generator.generate(library, component);
        log.debug(outputWriter.toString());

        verify(output);

        JavaSourceParser sourceParser = JavaSourceParser.parse(outputWriter.toString());
        assertTrue(sourceParser.containsMethod("isTestFlag"));
    }
}
