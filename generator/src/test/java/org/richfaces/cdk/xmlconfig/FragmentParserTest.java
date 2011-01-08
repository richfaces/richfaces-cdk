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

package org.richfaces.cdk.xmlconfig;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.As;
import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.PropertyBase;
import org.xml.sax.ext.EntityResolver2;

import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@RunWith(CdkTestRunner.class)
public class FragmentParserTest extends CdkTestBase {

    @Inject
    @As(JAXBBinding.class)
    JAXB binder;

    @Inject
    FragmentParser parser;

    @Inject
    @As(CdkEntityResolver.class)
    EntityResolver2 resolver;

    @Mock
    @Source(Sources.FACES_CONFIGS)
    private FileManager configSource;

    @Mock
    @Source(Sources.RENDERER_TEMPLATES)
    private FileManager templatesSource;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(CdkClassLoader.class).toInstance(createClassLoader());
    }

    @Test
    public void nestedXincludeTest() throws Exception {

        Collection<PropertyBase> properties =
            parser.parseProperties("urn:resource:org/richfaces/cdk/xmlconfig/parent2.xml");

        assertEquals(2, properties.size());
    }

    @Test
    public void parserTest() throws Exception {

        Collection<PropertyBase> properties =
            parser.parseProperties("urn:resource:org/richfaces/cdk/xmlconfig/fragment.xml");

        assertEquals(3, properties.size());
    }

    @Test
    public void propertyTest() throws Exception {

        Collection<PropertyBase> properties =
            parser.parseProperties("urn:resource:org/richfaces/cdk/xmlconfig/properties.xml");

        assertEquals(1, properties.size());

        PropertyBase property = Iterables.getOnlyElement(properties);
        assertEquals("ontest2", property.getName());

        assertEquals("int", property.getType().getName());
        assertEquals("test2 property", property.getDescription());
        assertEquals("ontest2.png", property.getIcon().getSmallIcon());
        assertEquals("test2 event property", property.getDisplayName());
        assertEquals("3", property.getDefaultValue());
        assertEquals("15", property.getSuggestedValue());

        // CDK extensions.
        assertTrue(property.getGenerate());
        assertTrue(property.isHidden());
        assertTrue(property.isLiteral());
        assertTrue(property.isPassThrough());
        assertTrue(property.isRequired());

        List<ClassName> signature = property.getSignature().getParameters();

        assertEquals(2, signature.size());

        String alias = property.getAliasFor();

        assertEquals("bar", alias);
    }

    @Test
    public void xincludeTest() throws Exception {

        Collection<PropertyBase> properties =
            parser.parseProperties("urn:resource:org/richfaces/cdk/xmlconfig/parent.xml");

        assertEquals(2, properties.size());
    }
}
