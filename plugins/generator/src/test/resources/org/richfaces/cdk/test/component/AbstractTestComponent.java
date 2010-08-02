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



package org.richfaces.cdk.test.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import java.util.List;

/**
 * <p class="changed_added_4_0"></p>
 * @author asmirnov@exadel.com
 *
 */
@JsfComponent(type = "org.richfaces.cdk.test.TestComponent")
public abstract class AbstractTestComponent extends UIComponent implements ValueHolder {
    private static final String COMPONENT_FAMILY = "org.richfaces.Test";
    @Attribute
    private int foo;

    /**
     * Test Attribute
     */
    @Attribute
    public abstract List<String> getTestValue();

    /**
     * Bar Attribute
     */
    @Attribute
    public abstract <M> void setBarValue(List<M> bar);
}
