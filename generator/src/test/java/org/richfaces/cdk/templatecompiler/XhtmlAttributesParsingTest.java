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

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.cdk.attributes.Attribute;
import org.richfaces.cdk.attributes.Element;
import org.richfaces.cdk.attributes.Schema;
import org.richfaces.cdk.attributes.SchemaSet;
import org.richfaces.cdk.attributes.Attribute.Kind;
import org.richfaces.cdk.templatecompiler.model.Template;

import com.google.common.base.Predicate;

/**
 * @author Nick Belaevski
 * 
 */
public class XhtmlAttributesParsingTest {

    private static final class AttributeKindPredicate implements Predicate<Attribute> {

        private Kind kind;

        public AttributeKindPredicate(Kind kind) {
            super();
            this.kind = kind;
        }

        @Override
        public boolean apply(Attribute attribute) {
            return kind.equals(attribute.getKind());
        }
    }

    private static final class AttributeNamePredicate implements Predicate<Attribute> {

        private String name;

        public AttributeNamePredicate(String name) {
            super();
            this.name = name;
        }

        @Override
        public boolean apply(Attribute attribute) {
            return name.equals(attribute.getName());
        }
    }

    private static final class AttributeNullDataPredicate implements Predicate<Attribute> {

        @Override
        public boolean apply(Attribute attribute) {
            return attribute.getName() == null || attribute.getKind() == null;
        }
    }

    private Schema xhtmlSchema;

    private Collection<Attribute> findAttributesByPredicate(Predicate<Attribute> predicate) {
        List<Attribute> result = new ArrayList<Attribute>();

        Collection<Element> elements = xhtmlSchema.getElements().values();
        for (Element element : elements) {
            Collection<Attribute> attributes = element.getAttributes().values();
            for (Attribute attribute : attributes) {
                if (predicate.apply(attribute)) {
                    result.add(attribute);
                }
            }
        }

        verifyAttributesByPredicate(result, predicate);

        return result;
    }

    private Collection<String> getNamesCollection(Collection<Attribute> attributes) {
        Collection<String> result = new TreeSet<String>();
        for (Attribute attribute : attributes) {
            result.add(attribute.getName());
        }

        return result;
    }

    @Before
    public void setUp() throws Exception {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        URL parsedSchemaDataResource = contextCL.getResource("META-INF/cdk/attributes/xhtml-el.xml");

        JAXBContext jc = JAXBContext.newInstance(SchemaSet.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        SchemaSet schemaSet = (SchemaSet) unmarshaller.unmarshal(parsedSchemaDataResource);
        // TODO make constants
        xhtmlSchema = schemaSet.getSchemas().get(Template.XHTML_EL_NAMESPACE);
    }

    @After
    public void tearDown() throws Exception {
        xhtmlSchema = null;
    }

    @Test
    public void testAttributeDataIsNonNull() throws Exception {
        Collection<Attribute> foundAttributes = findAttributesByPredicate(new AttributeNullDataPredicate());
        assertTrue(foundAttributes.isEmpty());
    }

    @Test
    public void testAttributeKinds() throws Exception {
        AttributeKindPredicate booleanAttributePredicate = new AttributeKindPredicate(Kind.BOOLEAN);
        AttributeKindPredicate genericAttributePredicate = new AttributeKindPredicate(Kind.GENERIC);
        AttributeKindPredicate uriAttributePredicate = new AttributeKindPredicate(Kind.URI);

        Collection<Attribute> booleanAttributes = findAttributesByPredicate(booleanAttributePredicate);
        Collection<Attribute> genericAttributes = findAttributesByPredicate(genericAttributePredicate);
        Collection<Attribute> uriAttributes = findAttributesByPredicate(uriAttributePredicate);

        Collection<String> booleanAttributesNames = getNamesCollection(booleanAttributes);
        Collection<String> genericAttributesNames = getNamesCollection(genericAttributes);
        Collection<String> uriAttributesNames = getNamesCollection(uriAttributes);

        System.out.println("XhtmlAttributesParsingTest.testAttributeKinds(), BOOLEAN attribute names: "
            + booleanAttributesNames);
        System.out.println("XhtmlAttributesParsingTest.testAttributeKinds(), URI attribute names: "
            + uriAttributesNames);

        for (String booleanAttributeName : booleanAttributesNames) {
            Collection<Attribute> foundAttributes =
                findAttributesByPredicate(new AttributeNamePredicate(booleanAttributeName));
            verifyAttributesByPredicate(foundAttributes, booleanAttributePredicate);
        }

        for (String genericAttributeName : genericAttributesNames) {
            Collection<Attribute> foundAttributes =
                findAttributesByPredicate(new AttributeNamePredicate(genericAttributeName));
            verifyAttributesByPredicate(foundAttributes, genericAttributePredicate);
        }

        for (String uriAttributeName : uriAttributesNames) {
            Collection<Attribute> foundAttributes =
                findAttributesByPredicate(new AttributeNamePredicate(uriAttributeName));
            verifyAttributesByPredicate(foundAttributes, uriAttributePredicate);
        }
    }

    @Test
    public void testRequiredAttributes() throws Exception {
        Collection<Attribute> requiredAttributes = findAttributesByPredicate(new Predicate<Attribute>() {

            @Override
            public boolean apply(Attribute input) {
                return input.isRequired();
            }
        });

        Collection<String> requiredAttributeNames = getNamesCollection(requiredAttributes);
        System.out.println("XhtmlAttributesParsingTest.testRequiredAttributes(): " + requiredAttributeNames);
    }

    private void verifyAttributesByPredicate(Collection<Attribute> attributes, Predicate<Attribute> predicate) {
        for (Attribute attribute : attributes) {
            Assert.assertTrue(attribute.getName(), predicate.apply(attribute));
        }
    }
}
