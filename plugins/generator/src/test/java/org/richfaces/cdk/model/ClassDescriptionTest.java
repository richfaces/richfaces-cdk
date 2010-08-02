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

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@RunWith(Parameterized.class)
public class ClassDescriptionTest {
    private final String className;
    private final String expectedBoxedName;
    private final String expectedCanonicalName;
    private final String expectedTypeParameter;
    private final String packageName;

    private final String simpleName;

    public ClassDescriptionTest(String className, String expectedCanonicalName, String expectedBoxedName,
        String expectedTypeParameter, String packageName, String simpleName) {
        this.className = className;
        this.expectedCanonicalName = expectedCanonicalName;
        this.expectedBoxedName = expectedBoxedName;
        this.expectedTypeParameter = expectedTypeParameter;
        this.packageName = packageName;
        this.simpleName = simpleName;
    }

    @Parameters
    public static Collection<String[]> values() {
        return Arrays.asList(new String[] { int.class.getName(), "int", "java.lang.Integer", null, null, "int" },
            new String[] { "java.util.List<String>", "java.util.List", "java.util.List<String>", "<String>","java.util", "List" },
            new String[] { double.class.getName(), "double", "java.lang.Double", null, null,"double" });
    }

    /**
     * Test method for {@link ClassName#ClassName(java.lang.String)}.
     */
    @Test
    public void testClassDescription() {
        ClassName description = new ClassName(className);

        assertEquals(className, description.toString());
        assertEquals(expectedCanonicalName, description.getName());
        assertEquals(expectedBoxedName, description.getBoxingName());
        assertEquals(expectedTypeParameter, description.getTypeParameters());
        assertEquals(packageName, description.getPackage());
        assertEquals(simpleName, description.getSimpleName());
    }
}
