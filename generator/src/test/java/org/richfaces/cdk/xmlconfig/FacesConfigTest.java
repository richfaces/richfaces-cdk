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
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.FacetModel;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.RenderKitModel;
import org.richfaces.cdk.xmlconfig.model.FacesConfigBean;
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
public class FacesConfigTest extends CdkTestBase {

    @Inject
    @As(JAXBBinding.class)
    JAXB jaxbBinding;

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
    public void testComponentUnmarshal() throws Exception {
        FacesConfigBean library =
            jaxbBinding.unmarshal("urn:resource:org/richfaces/cdk/xmlconfig/component.xml",
                ComponentLibrary.FACES_CONFIG_SCHEMA_LOCATION, FacesConfigBean.class);

        assertEquals(1, library.getComponents().size());

        ComponentModel component = library.getComponents().get(0);

        assertEquals(FacesId.parseId("javax.faces.Panel"), component.getId());
        assertEquals(FacesId.parseId("javax.faces.Panel"), component.getFamily());
        assertEquals(ClassName.parseName("javax.faces.component.UIPanel"), component.getTargetClass());
        assertEquals("panel.gif", component.getIcon().getSmallIcon());
        assertEquals("panel-large.gif", component.getIcon().getLargeIcon());
        assertEquals("Panel component", component.getDescription());
        assertEquals("Panel", component.getDisplayname());
        assertTrue(component.isGenerate());

        FacetModel facet = Iterables.getOnlyElement(component.getFacets());
        assertEquals("header", facet.getName());
        assertEquals("Header facet", facet.getDescription());
        assertTrue(facet.isGenerate());

        Collection<PropertyBase> attributes = component.getAttributes();

        assertEquals(3, attributes.size());
    }

    @Test
    public void testRenderKitUnmarshall() throws Exception {

        FacesConfigBean library =
            jaxbBinding.unmarshal("urn:resource:org/richfaces/cdk/xmlconfig/renderkit.xml",
                ComponentLibrary.FACES_CONFIG_SCHEMA_LOCATION, FacesConfigBean.class);
        List<RenderKitModel> renderKits = library.getRenderKits();
        assertEquals(2, renderKits.size());

        RenderKitModel renderKit = renderKits.get(0);
        assertEquals(new FacesId("HTML_BASIC"), renderKit.getId());
        assertEquals(1, renderKit.getRenderers().size());

        assertEquals(new FacesId("RDFa"), renderKits.get(1).getId());
    }
}
