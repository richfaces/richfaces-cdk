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
package org.richfaces.cdk.templatecompiler.el.types;

import org.richfaces.cdk.util.Strings;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class DummyPropertyDescriptor implements ELPropertyDescriptor {
    private final String propertyName;
    private final String getterName;
    private final String setterName;

    public DummyPropertyDescriptor(String propertyName) {
        this.propertyName = propertyName;
        String firstToUpperCase = Strings.firstToUpperCase(propertyName);
        this.getterName = "get" + firstToUpperCase;
        this.setterName = "set" + firstToUpperCase;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor#getName()
     */
    @Override
    public String getName() {
        return propertyName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor#getType()
     */
    @Override
    public ELType getType() {
        return TypesFactory.OBJECT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor#isReadable()
     */
    @Override
    public boolean isReadable() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor#isWritable()
     */
    @Override
    public boolean isWritable() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor#getReadMethodName()
     */
    @Override
    public String getReadMethodName() {
        return getterName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor#getWriteMethosName()
     */
    @Override
    public String getWriteMethosName() {
        return setterName;
    }
}
