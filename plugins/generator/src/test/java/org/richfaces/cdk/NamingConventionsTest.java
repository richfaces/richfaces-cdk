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

package org.richfaces.cdk;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.FacesId;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class NamingConventionsTest {
    private static final String BASE = "foo.bar";
    private static final FacesId COMPONENT_TYPE = FacesId.parseId("foo.bar.Test");
    private RichFacesConventions conventions;

    @Before
    public void createConventions() {
        conventions = new RichFacesConventions();
    }

    @After
    public void destroyConventions() {
        conventions = null;
    }

    @Test
    public void testInferComponentTypeFromAbstractClass() throws Exception {
        assertEquals(COMPONENT_TYPE, conventions.inferComponentType(new ClassName("foo.bar.component.AbstractTest")));
    }

    @Test
    public void testInferComponentTypeFromBaseClass() throws Exception {
        assertEquals(COMPONENT_TYPE, conventions.inferComponentType(new ClassName("foo.bar.component.TestBase")));
    }

    @Test
    public void testInferComponentTypeFromMarkupClass() throws Exception {
        assertEquals(FacesId.parseId("foo.bar.HtmlTest"), conventions.inferComponentType(new ClassName(
            "foo.bar.component.html.HtmlTest")));
    }

    /**
     * Test method for
     * {@link org.richfaces.cdk.RichFacesConventions#inferComponentType(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testInferComponentTypeFromUIClass() throws Exception {
        assertEquals(COMPONENT_TYPE, conventions.inferComponentType(new ClassName("foo.bar.component.UITest")));
    }

    @Test
    public void testInferUIComponentClassFromType() throws Exception {
        assertEquals(new ClassName("foo.bar.component.UITest"), conventions.inferUIComponentClass(COMPONENT_TYPE));
    }
}
