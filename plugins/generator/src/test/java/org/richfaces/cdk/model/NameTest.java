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

package org.richfaces.cdk.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class NameTest {

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateString() throws Exception {
        Name name = Name.create("foo.bar.component.wml.WmlFoo");

        assertEquals("foo.bar", name.getPrefix());
        assertEquals(Name.Classifier.component, name.getClassifier());
        assertEquals("wml", name.getMarkup());
        assertEquals("WmlFoo", name.getSimpleName());
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateString1() throws Exception {
        Name name = Name.create("foo.bar.wml.WmlFoo");

        assertEquals("foo.bar.wml", name.getPrefix());
        assertNull(name.getClassifier());
        assertNull(name.getMarkup());
        assertEquals("WmlFoo", name.getSimpleName());
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateString2() throws Exception {
        Name name = Name.create("foo.bar.component.Foo");

        assertEquals("foo.bar", name.getPrefix());
        assertEquals(Name.Classifier.component, name.getClassifier());
        assertNull(name.getMarkup());
        assertEquals("Foo", name.getSimpleName());
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateString3() throws Exception {
        Name name = Name.create("Foo");

        assertNull(name.getPrefix());
        assertNull(name.getClassifier());
        assertNull(name.getMarkup());
        assertEquals("Foo", name.getSimpleName());
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateString4() throws Exception {
        Name name = Name.create("component.Foo");

        assertNull(name.getPrefix());
        assertEquals(Name.Classifier.component, name.getClassifier());
        assertNull(name.getMarkup());
        assertEquals("Foo", name.getSimpleName());
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateString5() throws Exception {
        Name name = Name.create("component.wml.Foo");

        assertNull(name.getPrefix());
        assertEquals(Name.Classifier.component, name.getClassifier());
        assertEquals("wml", name.getMarkup());
        assertEquals("Foo", name.getSimpleName());
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateString6() throws Exception {
        Name name = Name.create("bar.component.wml.Foo");

        assertEquals("bar", name.getPrefix());
        assertEquals(Name.Classifier.component, name.getClassifier());
        assertEquals("wml", name.getMarkup());
        assertEquals("Foo", name.getSimpleName());
    }

    /**
     * Test method for
     * {@link org.richfaces.cdk.model.Name#create(java.lang.String, org.richfaces.cdk.model.Name.Classifier, java.lang.String)}
     * .
     */
    @Test
    public void testCreateStringClassifierString() {

        // fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.richfaces.cdk.model.Name#create(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testCreateStringString() {

        // fail("Not yet implemented");
    }
}
