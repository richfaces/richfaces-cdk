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
package org.richfaces.cdk.templatecompiler.statements;

import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.*;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 * 
 */
public enum HelperMethod {

    TO_STRING_CONVERSION(CONVERT_TO_STRING_FUNCTION, String.class, Object.class),
    TO_BOOLEAN_CONVERSION(CONVERT_TO_BOOLEAN_FUNCTION, Boolean.TYPE, Object.class),
    EMPTINESS_CHECK(IS_EMPTY_FUNCTION, Boolean.TYPE, Object.class),
    EQUALS_CHECK(IS_EQUAL_FUNCTION, Boolean.TYPE, Object.class, Object.class),
    SHOULD_RENDER_ATTRIBUTE("shouldRenderAttribute", Boolean.TYPE, Object.class),
    CREATE_ATTRIBUTES("attributes", "Attributes"),
    RENDER_ATTRIBUTES_SET("renderPassThroughAttributes", Void.TYPE,FacesContext.class,UIComponent.class,Collection.class),
    RENDER_ATTRIBUTE("renderAttribute",Void.TYPE,FacesContext.class,String.class,Object.class);

    public static final EnumMap<HelperMethod, String> METHOD_NAMES = Maps.newEnumMap(HelperMethod.class);

    private static final Set<HelperMethod> CONVERSION_METHODS = EnumSet.of(TO_STRING_CONVERSION, TO_BOOLEAN_CONVERSION);
    
    private final String name;

    private final String returnType;
    
    private final Class<?>[] argumentTypes;
    
    static {
        for (HelperMethod method : HelperMethod.values()) {
            METHOD_NAMES.put(method, method.getName());
        }
    }
    
    private HelperMethod(String name, String returnType, Class<?>... argumentTypes) {
        this.name = name;
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
    }
    
    private HelperMethod(String name, Class<?> returnType, Class<?>... argumentTypes) {
        this(name,returnType.getName(),argumentTypes);
    }

    public String getName() {
        return name;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }
    
    public static Set<HelperMethod> getConversionMethods() {
        return CONVERSION_METHODS;
    }
}
