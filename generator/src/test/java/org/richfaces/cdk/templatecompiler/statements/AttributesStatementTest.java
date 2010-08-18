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

package org.richfaces.cdk.templatecompiler.statements;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.MockController;
import org.richfaces.cdk.attributes.Attribute;
import org.richfaces.cdk.attributes.Attribute.Kind;
import org.richfaces.cdk.attributes.Element;
import org.richfaces.cdk.attributes.Schema;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.ModelSet;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.PropertyModel;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.model.AnyElement;
import org.richfaces.cdk.templatecompiler.model.Template;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.name.Names;

/**
 * <p class="changed_added_4_0"></p>
 * @author asmirnov@exadel.com
 *
 */
@RunWith(CdkTestRunner.class)
public class AttributesStatementTest extends CdkTestBase {

    @Mock
    private Logger log;
    
    @Mock
    private ELParser parser;
    @Mock
    private TypedTemplateStatement parsedExpression;
    
    @Mock 
    @TemplateModel
    private FreeMarkerRenderer renderer;
        
    @Inject
    private AttributesStatement statement;
    
    @Inject
    private MockController controller;
    
    
    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        Schema schema = new Schema();
        Element element = new Element("div");
        createSchemaAttribute(element, "class",null,Kind.GENERIC);
        createSchemaAttribute(element, "href",null,Kind.URI);
        createSchemaAttribute(element, "disabled",null,Kind.BOOLEAN);
        schema.addElement(element);
        binder.bind(Schema.class).annotatedWith(Names.named(Template.XHTML_EL_NAMESPACE)).toInstance(schema);
    }
    private void createSchemaAttribute(Element element, String name,String defaultValue,Kind kind) {
        Attribute attribute = new Attribute(name);
        attribute.setDefaultValue(defaultValue);
        attribute.setComponentAttributeName(name+"Component");
        attribute.setKind(kind);
        element.addAttribute(attribute);
    }
    /**
     * Test method for {@link org.richfaces.cdk.templatecompiler.statements.AttributesStatement#processAttributes(java.util.Map, java.util.Collection)}.
     * @throws Exception 
     */
    @Test
    public void testProcessSimpleHtmlAttribute() throws Exception {
        expect(parser.parse(eq("header"), isA(WriteAttributeStatement.class), same(TypesFactory.OBJECT_TYPE))).andReturn(parsedExpression);
        parsedExpression.setParent(isA(WriteAttributeStatement.class));expectLastCall();
        processAttributes("div", "id", "header");
        assertEquals(1, statement.getStatements().size());
        assertThat(statement.getStatements().get(0), instanceOf(WriteAttributeStatement.class));
    }
    private void processAttributes(String element, String attribute, String value)  throws Exception {
        Collection<PropertyBase> componentAttributes = createComponentAttributes();
        processAttributes(element, attribute, value, componentAttributes);        
    }
    private void processAttributes(String element, String attribute, String value, Collection<PropertyBase> componentAttributes)  throws Exception {
        controller.replay();
        AnyElement anyElement = new AnyElement();
        anyElement.setName(QName.valueOf(element));
        anyElement.getAttributes().putAll(createAttributesMap(attribute, value));
        statement.processAttributes(anyElement, componentAttributes);
        controller.verify();
    }
    
    /**
     * Test method for {@link org.richfaces.cdk.templatecompiler.statements.AttributesStatement#processAttributes(java.util.Map, java.util.Collection)}.
     */
    @Test
    public void testProcessLiteralAttribute() {
        
    }
    /**
     * Test method for {@link org.richfaces.cdk.templatecompiler.statements.AttributesStatement#processAttributes(java.util.Map, java.util.Collection)}.
     */
    @Test
    public void testProcessElAttribute() {
        
    }
    /**
     * Test method for {@link org.richfaces.cdk.templatecompiler.statements.AttributesStatement#processAttributes(java.util.Map, java.util.Collection)}.
     */
    @Test
    public void testProcessHtmlAttributeWithBehavior() {
        
    }

    private Map<QName, Object> createAttributesMap(String name, String value){
        ImmutableMap<QName,Object> map = ImmutableMap.<QName, Object>of(QName.valueOf(name),value);
        return map;
    }

    private PropertyBase createComponentAttribute(String name,String ...events) {
        PropertyBase property = new PropertyModel();
        property.setName(name);
        for (String event : events) {
            EventName eventName = new EventName();
            eventName.setName(event);
            property.getEventNames().add(eventName);
        }
        return property;
    }

    private Collection<PropertyBase> createComponentAttributes(PropertyBase ...properties) {
        ModelSet<PropertyBase> attributes = ModelSet.<PropertyBase>create();
        for (PropertyBase prop : properties) {
            attributes.add(prop);
        }
        return attributes;
    }
}
