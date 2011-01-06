/*
 * $Id: TestSubClass.java 18741 2010-08-18 03:07:27Z alexsmirnov $
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



package org.richfaces.cdk.apt;

import org.richfaces.cdk.apt.TestMethodAnnotation;

/**
 * <p class="changed_added_4_0">Java Bean to test bean properties processor.
 * that class contains next properties:
 * <ol>
 * <li>r/w concreteValue, type String, annotated with {@link TestMethodAnnotation} "setter"</li>
 * <li> r/w inheritedValue, type String, inherited from {@link PropertyTestInterface}, annotated {@link TestMethodAnnotation} "inherited" </li> 
 * <li> abstract r/w value, inherited from {@link TestInterface},{@link TestMethodAnnotation} "baz"</li>
 * <li> r readOnly, type Integer, inherited from {@link TestClass}</li>
 * <li> w writeOnly, type Integer, inherited from {@link TestClass}</li>
 * <li> wrongValue that should be ignored with warning message.</li>
 * </ol>
 * </p>
 * @author asmirnov@exadel.com
 */
public class PropertyTestClass extends TestClass implements TestInterface, PropertyTestInterface {
    private String concreteValue;

    /**
     * <p class="changed_added_4_0"></p>
     * @param value the value to set
     */
    @TestMethodAnnotation("setter")
    public void setConcreteValue(String value) {
        this.value = concreteValue;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the value
     */
    public String getConcreteValue() {
        return concreteValue;
    }


    public void setInheritedValue(String value) {
        this.value = concreteValue;
    }

    public String getInheritedValue() {
        return concreteValue;
    }

    public void setWrongValue(Integer value) {

    }

}
