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

import static com.google.common.collect.Iterators.forArray;
import static com.google.common.collect.Iterators.toArray;
import static com.google.common.collect.Iterators.transform;
import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.CONVERT_TO_BOOLEAN_FUNCTION;
import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.CONVERT_TO_STRING_FUNCTION;
import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.IS_EMPTY_FUNCTION;
import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.IS_EQUAL_FUNCTION;
import static org.richfaces.cdk.util.JavaUtils.CLASS_TO_CLASS_NAME;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
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
    RENDER_ATTRIBUTE("renderAttribute",Void.TYPE,FacesContext.class,String.class,Object.class),
    ADD_TO_SCRIPT_HASH("addToScriptHash", Void.TYPE.getName(), Map.class.getName(), String.class.getName(), 
        Object.class.getName(), Object.class.getName(), "ScriptHashVariableWrapper"),
    ADD_TO_SCRIPT_HASH_ATTRIBUTES("addToScriptHash", Void.TYPE.getName(), Map.class.getName(), 
        FacesContext.class.getName(), UIComponent.class.getName(), 
        "Attributes", "ScriptHashVariableWrapper"),
    TO_SCRIPT_ARGS("toScriptArgs", String.class, Object[].class),
    CONCAT("concat", String.class, String[].class);

    public static final EnumMap<HelperMethod, String> METHOD_NAMES = Maps.newEnumMap(HelperMethod.class);

    private static final Set<HelperMethod> CONVERSION_METHODS = EnumSet.of(TO_STRING_CONVERSION, TO_BOOLEAN_CONVERSION);
    
    private final String name;

    private final String returnType;
    
    private final String[] argumentTypes;
    
    static {
        for (HelperMethod method : HelperMethod.values()) {
            METHOD_NAMES.put(method, method.getName());
        }
    }
    
    private HelperMethod(String name, String returnType, String... argumentTypes) {
        this.name = name;
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
    }
    
    private HelperMethod(String name, Class<?> returnType, Class<?>... argumentTypes) {
        this(name, CLASS_TO_CLASS_NAME.apply(returnType), transformClassesToClassNames(argumentTypes));
    }

    private static String[] transformClassesToClassNames(Class<?>[] s) {
        Iterator<String> transformed = transform(forArray(s), CLASS_TO_CLASS_NAME);
        return toArray(transformed, String.class);
    }

    public String getName() {
        return name;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public String[] getArgumentTypes() {
        return argumentTypes;
    }
    
    public static Set<HelperMethod> getConversionMethods() {
        return CONVERSION_METHODS;
    }
}
