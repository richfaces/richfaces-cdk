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

import java.util.Set;

import org.junit.Test;
import org.richfaces.cdk.xmlconfig.testmodel.Child;
import org.richfaces.cdk.xmlconfig.testmodel.Root;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class JaxbUnmarshalTest extends JaxbTestBase {
    @Test
    public void testExtensions() throws Exception {
        Root root =
            unmarshal(
                Root.class,
                "<root-config xmlns='http://foo.bar/schema' ><name>foo</name><children><id>xxx</id><value>bar</value><extension><e:myExtension xmlns:e=\"http://foo.bar/extensions\">eee</e:myExtension><s:foo xmlns:s=\"urn:foo\">foo</s:foo></extension></children></root-config>");
        Set<Child> children = root.getChildren();

        assertEquals(1, children.size());

        Child child = children.iterator().next();

        assertEquals(1, child.getExtension().getExtensions().size());
        assertEquals("eee", child.getWrapped());
    }

    @Test
    public void testRootElement() throws Exception {
        Root root =
            unmarshal(
                Root.class,
                "<root-config xmlns='http://foo.bar/schema' ><name>foo</name><children><id>xxx</id><value>bar</value></children></root-config>");

        assertEquals("foo", root.getName());

        Set<Child> children = root.getChildren();

        assertEquals(1, children.size());
        assertEquals("xxx", children.iterator().next().getId());
    }

    @Test
    public void testUniqueElement() throws Exception {
        Root root =
            unmarshal(
                Root.class,
                "<root-config xmlns='http://foo.bar/schema' ><name>foo</name><children><id>xxx</id><value>bar</value></children><children><id>xxx</id><value>baz</value></children></root-config>");

        assertEquals("foo", root.getName());

        Set<Child> children = root.getChildren();

        assertEquals(1, children.size());
        assertEquals("xxx", children.iterator().next().getId());
    }
}
