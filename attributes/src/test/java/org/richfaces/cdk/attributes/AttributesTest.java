/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.richfaces.cdk.attributes.Attribute.Kind;

/**
 * @author Nick Belaevski
 * 
 */
public class AttributesTest {

    private SchemaSet schemaSet;
    
    private String marshal(SchemaSet schemaSet) throws Exception {
        StringWriter writer = new StringWriter();
        JAXBContext jc = JAXBContext.newInstance(SchemaSet.class);
        Marshaller marshaller = jc.createMarshaller();
        
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(schemaSet, new StreamResult(writer));

        return writer.toString();
    }
    
    private SchemaSet unmarshal(String xmlData) throws Exception {
        StringReader reader = new StringReader(xmlData);
        JAXBContext jc = JAXBContext.newInstance(SchemaSet.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        return (SchemaSet) unmarshaller.unmarshal(reader);
    }

    @Before
    public void setUp() {
        schemaSet = new SchemaSet();
        Schema schema = new Schema("urn:abc");
        schemaSet.addSchema(schema);
        
        Element spanElement = new Element("span");
        schema.addElement(spanElement);

        Attribute idAttribute = new Attribute("id");
        spanElement.addAttribute(idAttribute);
        
        Element imgElement = new Element("img");
        schema.addElement(imgElement);

        Attribute altAttribute = new Attribute("alt");
        altAttribute.setRequired(true);
        imgElement.addAttribute(altAttribute);
        
        Attribute srcAttribute = new Attribute("src");
        srcAttribute.setKind(Kind.URI);
        imgElement.addAttribute(srcAttribute);

        Attribute dirAttribute = new Attribute("dir");
        dirAttribute.setDefaultValue("ltr");
        imgElement.addAttribute(dirAttribute);

        Attribute classAttribute = new Attribute("class");
        classAttribute.setComponentAttributeName("styleClass");
        imgElement.addAttribute(classAttribute);
    }
    
    public void tearDown() {
        schemaSet = null;
    }
    
    @Test
    public void testMarshalUnmarshal() throws Exception {
        String marshalledData = marshal(schemaSet);
        
        assertNotNull(marshalledData);
        assertTrue(marshalledData.length() > 0);
        
        SchemaSet restoredSchemaSet = unmarshal(marshalledData);
        assertNotNull(restoredSchemaSet);
        
        assertEquals(1, restoredSchemaSet.getSchemas().size());
        Schema restoredSchema = restoredSchemaSet.getSchemas().get("urn:abc");
        assertEquals("urn:abc", restoredSchema.getNamespace());
        assertEquals("urn:abc", restoredSchema.getKey());
    
        Map<String, Element> restoredElements = restoredSchema.getElements();
        assertEquals(2, restoredElements.size());
        
        Element restoredSpanElement = restoredElements.get("span");
        assertNotNull(restoredSpanElement);
        assertEquals("span", restoredSpanElement.getName());
        assertEquals("span", restoredSpanElement.getKey());
        Map<String, Attribute> restoredSpanAttributes = restoredSpanElement.getAttributes();
        assertEquals(1, restoredSpanAttributes.size());
        Attribute restoredIdAttribute = restoredSpanAttributes.get("id");
        assertNotNull(restoredIdAttribute);
        assertEquals("id", restoredIdAttribute.getName());
        assertEquals("id", restoredIdAttribute.getComponentAttributeName());
        assertEquals("id", restoredIdAttribute.getKey());
        assertNull(restoredIdAttribute.getDefaultValue());
        assertFalse(restoredIdAttribute.isRequired());
        assertEquals(Kind.GENERIC, restoredIdAttribute.getKind());
        
        Element restoredImgElement = restoredElements.get("img");
        assertNotNull(restoredImgElement);
        assertEquals("img", restoredImgElement.getName());
        assertEquals("img", restoredImgElement.getKey());
        Map<String, Attribute> restoredImgAttributes = restoredImgElement.getAttributes();
        assertEquals(4, restoredImgAttributes.size());

        Attribute restoredAltAttribute = restoredImgAttributes.get("alt");
        assertNotNull(restoredAltAttribute);
        assertEquals("alt", restoredAltAttribute.getName());
        assertEquals("alt", restoredAltAttribute.getComponentAttributeName());
        assertTrue(restoredAltAttribute.isRequired());

        Attribute restoredSrcAttribute = restoredImgAttributes.get("src");
        assertNotNull(restoredSrcAttribute);
        assertEquals("src", restoredSrcAttribute.getName());
        assertEquals("src", restoredSrcAttribute.getComponentAttributeName());
        assertEquals(Kind.URI, restoredSrcAttribute.getKind());

        Attribute restoredDirAttribute = restoredImgAttributes.get("dir");
        assertNotNull(restoredDirAttribute);
        assertEquals("dir", restoredDirAttribute.getName());
        assertEquals("dir", restoredDirAttribute.getComponentAttributeName());
        assertEquals("ltr", restoredDirAttribute.getDefaultValue());

        Attribute restoredClassAttribute = restoredImgAttributes.get("class");
        assertNotNull(restoredClassAttribute);
        assertEquals("class", restoredClassAttribute.getName());
        assertEquals("styleClass", restoredClassAttribute.getComponentAttributeName());
    }
}
