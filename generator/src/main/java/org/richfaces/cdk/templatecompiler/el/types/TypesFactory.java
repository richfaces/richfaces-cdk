/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.templatecompiler.el.types;

import org.richfaces.cdk.templatecompiler.el.ParsingException;

/**
 * @author Nick Belaevski
 * @author Lukas Fryc
 *
 */
public interface TypesFactory {
    ELType OBJECT_TYPE = new PlainClassType(Object.class);
    ELType STRING_TYPE = new PlainClassType(String.class);
    ELType BOOLEAN_TYPE = new PlainClassType(Boolean.TYPE);
    ELType DOUBLE_TYPE = new PlainClassType(Double.TYPE);
    ELType INTEGER_TYPE = new PlainClassType(Integer.class);
    ELType INT_TYPE = new PlainClassType(Integer.TYPE);
    ELType VOID_TYPE = new PlainClassType(Void.TYPE);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param typeString
     * @return
     */
    ELType getType(String typeString);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param reflectionType
     * @return
     */
    ELType getType(java.lang.reflect.Type reflectionType);

    ELType getGeneratedType(String typeString, ELType superType);

    /**
     * <p>
     * Find an accessible method that matches the given name and has compatible parameters. Compatible parameters mean that
     * every method parameter is assignable from the given parameters. In other words, it finds a method with the given name
     * that will take the parameters given.
     * <p>
     *
     * <p>
     * This method is slightly undeterminstic since it loops through methods names and return the first matching method.
     * </p>
     *
     * <p>
     * This method is used by {@link #invokeMethod(Object object, String methodName, Object [] args, Class[] parameterTypes)}.
     *
     * <p>
     * This method can match primitive parameter by passing in wrapper classes. For example, a <code>Boolean</code> will match a
     * primitive <code>boolean</code> parameter.
     *
     * @param elType find method in this class
     * @param methodName find method with this name
     * @param parameterTypes find method with compatible parameters
     * @return The accessible method
     * @throws ParsingException if error occured.
     */
    ELType getMatchingVisibleMethodReturnType(ELType elType, final String methodName, ELType... parameterTypes)
            throws ParsingException;

    /**
     * This method return PropertyDescriptor by specified propertyName and clazz.
     *
     * @param elType - class to search
     * @param propertyName - propertyName to search
     * @return property descriptor if found.
     * @throws ParsingException if error occured.
     */
    ELPropertyDescriptor getPropertyDescriptor(ELType type, String name) throws ParsingException;
}
