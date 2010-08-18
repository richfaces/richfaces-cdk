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
package org.richfaces.cdk.templatecompiler.parser.el.test;

import static org.junit.Assert.*;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactoryImpl;

import com.google.inject.Binder;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 * 
 */
@RunWith(CdkTestRunner.class)
public class TypesFactoryTest extends CdkTestBase {

    @Inject
    TypesFactoryImpl typesFactory;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(CdkClassLoader.class).toInstance(createClassLoader());
    }

    private static final class ParameterizedTypesHolder {

        @SuppressWarnings("unused")
        public List<String>[] getArray() {
            return null;
        }

        @SuppressWarnings("unused")
        public Map<String, Object> getMap() {
            return null;
        }

        @SuppressWarnings("unused")
        public <Abc> Abc getTypeVariableList() {
            return null;
        }

        @SuppressWarnings("unused")
        public List<? extends String> getWildcardList() {
            return null;
        }
    }

    private java.lang.reflect.Type getParameterizedArrayType() throws Exception {
        return ParameterizedTypesHolder.class.getMethod("getArray").getGenericReturnType();
    }

    private java.lang.reflect.Type getParameterizedMapType() throws Exception {
        return ParameterizedTypesHolder.class.getMethod("getMap").getGenericReturnType();
    }

    private java.lang.reflect.Type getTypeVariableType() throws Exception {
        return ParameterizedTypesHolder.class.getMethod("getTypeVariableList").getGenericReturnType();
    }

    private java.lang.reflect.Type getWildcardType() throws Exception {
        return ParameterizedTypesHolder.class.getMethod("getWildcardList").getGenericReturnType();
    }

    @Test
    public void testCaching() throws Exception {
        ELType objectType = typesFactory.getType(Object.class);
        ELType objectType2 = typesFactory.getType(Object.class);

        assertNotNull(objectType);
        assertNotNull(objectType2);

        assertSame(objectType, objectType2);

    }

    @Test
    public void testGetTypeFromReflectionType() throws Exception {
        ELType integerType = typesFactory.getType(Integer.TYPE);
        assertNotNull(integerType);
        assertEquals(Integer.TYPE.getName(), integerType.getRawName());
        assertFalse(integerType.isArray());
        assertFalse(integerType.isNullType());

        ELType stringType = typesFactory.getType(String.class);
        assertNotNull(stringType);
        assertEquals(String.class.getName(), stringType.getRawName());
        assertEquals(0, stringType.getTypeArguments().length);
        assertFalse(stringType.isArray());
        assertFalse(stringType.isNullType());

        ELType arrayType = typesFactory.getType(String[].class);
        assertNotNull(arrayType);
        assertEquals(0, arrayType.getTypeArguments().length);
        assertTrue(arrayType.isArray());
        assertEquals(String.class.getName(), arrayType.getContainerType().getRawName());
        assertFalse(arrayType.isNullType());

        ELType multiDimArrayType = typesFactory.getType(String[][][].class);
        assertNotNull(multiDimArrayType);
        assertEquals(0, multiDimArrayType.getTypeArguments().length);
        assertTrue(multiDimArrayType.isArray());
        // assertEquals(String[][].class, multiDimArrayType.getContainerType().getRawName());
        assertFalse(multiDimArrayType.isNullType());

        ELType parameterizedMapType = typesFactory.getType(getParameterizedMapType());
        assertNotNull(parameterizedMapType);
        assertFalse(parameterizedMapType.isArray());
        assertEquals(Map.class.getName(), parameterizedMapType.getRawName());

        ELType[] parameterizedMapTypeArguments = parameterizedMapType.getTypeArguments();
        assertNotNull(parameterizedMapTypeArguments);
        assertEquals(2, parameterizedMapTypeArguments.length);
        assertEquals(String.class.getName(), parameterizedMapTypeArguments[0].getRawName());
        assertEquals(Object.class.getName(), parameterizedMapTypeArguments[1].getRawName());

        ELType parameterizedArrayType = typesFactory.getType(getParameterizedArrayType());
        assertNotNull(parameterizedArrayType);
        assertTrue(parameterizedArrayType.isArray());
        // assertEquals(List[].class, parameterizedArrayType.getRawName());

        ELType[] parameterizedArrayTypeArguments = parameterizedArrayType.getTypeArguments();
        assertNotNull(parameterizedArrayTypeArguments);
        assertEquals(1, parameterizedArrayTypeArguments.length);
        ELType parameterizedArrayTypeArgument = parameterizedArrayTypeArguments[0];
        assertEquals(String.class.getName(), parameterizedArrayTypeArgument.getRawName());
        assertFalse(parameterizedArrayTypeArgument.isArray());

        ELType typeVariableType = typesFactory.getType(getTypeVariableType());

        assertNotNull(typeVariableType);
        assertEquals("Abc", typeVariableType.getCode());

        ELType wildcardTypeHolder = typesFactory.getType(getWildcardType());
        assertNotNull(wildcardTypeHolder);
        assertEquals(List.class.getName(), wildcardTypeHolder.getRawName());

        ELType[] wildcardTypeHolderArguments = wildcardTypeHolder.getTypeArguments();
        assertNotNull(wildcardTypeHolderArguments);
        assertEquals(1, wildcardTypeHolderArguments.length);
        ELType wildcardType = wildcardTypeHolderArguments[0];
        assertEquals("? extends java.lang.String", wildcardType.getCode());
    }

    @Test
    public void testGetTypeFromString() throws Exception {

        ELType primitiveIntType = typesFactory.getType("int");
        assertNotNull(primitiveIntType);
        assertEquals(Integer.TYPE.getName(), primitiveIntType.getRawName());

        ELType guessedMapType = typesFactory.getType("Map");
        assertNotNull(guessedMapType);
        assertEquals(Map.class.getName(), guessedMapType.getRawName());

        ELType writerType = typesFactory.getType(java.io.Writer.class.getName());
        assertNotNull(writerType);
        assertEquals(Writer.class.getName(), writerType.getRawName());
        assertEquals(0, writerType.getTypeArguments().length);

        ELType genericMapType = typesFactory.getType("Map<String, Object>");
        assertNotNull(genericMapType);

        assertEquals(Map.class.getName(), genericMapType.getRawName());
        ELType[] genericMapTypeArguments = genericMapType.getTypeArguments();
        assertNotNull(genericMapTypeArguments);
        assertFalse(genericMapType.isArray());
        assertEquals(2, genericMapTypeArguments.length);

        ELType genericMapTypeKeyArgument = genericMapTypeArguments[0];
        assertEquals(String.class.getName(), genericMapTypeKeyArgument.getRawName());

        ELType genericMapTypeValueArgument = genericMapTypeArguments[1];
        assertEquals(Object.class.getName(), genericMapTypeValueArgument.getRawName());

        ELType arrayType = typesFactory.getType("String[]");
        assertNotNull(arrayType);
        assertTrue(arrayType.isArray());
        // assertEquals(String[].class, arrayType.getRawName());

        ELType genericArrayType = typesFactory.getType("List<String>[]");
        assertNotNull(genericArrayType);
        assertTrue(genericArrayType.isArray());
        // assertEquals(List[].class, genericArrayType.getRawName());

        ELType[] genericArrayTypeArguments = genericArrayType.getTypeArguments();
        assertNotNull(genericArrayTypeArguments);
        assertEquals(1, genericArrayTypeArguments.length);

        ELType genericArrayTypeArgument = genericArrayTypeArguments[0];
        assertEquals(String.class.getName(), genericArrayTypeArgument.getRawName());
    }

    @Test
    public void testReferencedType() throws Exception {
        String className = "some.not.available.Class";
        ELType plainReferencedType = typesFactory.getType(className);

        assertNotNull(plainReferencedType);
        assertEquals(className, plainReferencedType.getCode());

        String arraySignature = className + "[]";
        ELType arrayReferencedType = typesFactory.getType(arraySignature);
        assertNotNull(arrayReferencedType);
        assertTrue(arrayReferencedType.isArray());
        assertEquals(arraySignature, arrayReferencedType.getCode());

        String genericSignature = className + "<String>";
        ELType genericReferenceType = typesFactory.getType(genericSignature);
        assertNotNull(genericReferenceType);
        assertEquals(genericSignature, genericReferenceType.getCode());

        ELType[] genericTypeArguments = genericReferenceType.getTypeArguments();
        assertNotNull(genericTypeArguments);
        assertEquals(1, genericTypeArguments.length);
        ELType genericTypeArgument = genericTypeArguments[0];
        assertEquals(String.class.getName(), genericTypeArgument.getRawName());
    }

    public ELPropertyDescriptor getAndCheckPropertyDescriptor(Class<?> clazz, String propertyName,
        String getMethodName, Class<?> expectedType, boolean readable, boolean writable) throws Exception {
        ELPropertyDescriptor checkPropertyDescriptor =
            getAndCheckPropertyDescriptor(clazz, propertyName, getMethodName, expectedType);
        assertEquals(writable, checkPropertyDescriptor.isWritable());
        assertEquals(readable, checkPropertyDescriptor.isReadable());
        return checkPropertyDescriptor;
    }

    public ELPropertyDescriptor getAndCheckPropertyDescriptor(Class<?> clazz, String propertyName,
        String getMethodName, Class<?> expectedType) throws Exception {
        ELType beanType = typesFactory.getType(clazz);
        ELPropertyDescriptor propertyDescriptor = typesFactory.getPropertyDescriptor(beanType, propertyName);
        assertNotNull(propertyDescriptor);
        assertEquals(getMethodName, propertyDescriptor.getReadMethodName());
        assertEquals(expectedType.getName(), propertyDescriptor.getType().getRawName());
        return propertyDescriptor;
    }

    @Test
    public void testGetStringProperty() throws Exception {
        ELPropertyDescriptor propertyDescriptor =
            getAndCheckPropertyDescriptor(Bean2.class, "string", "getString", String.class, true, true);
    }

    @Test
    public void testGetRawMapProperty() throws Exception {
        ELPropertyDescriptor propertyDescriptor =
            getAndCheckPropertyDescriptor(Bean.class, "rawMap", "getRawMap", Map.class, true, true);
    }

    @Test
    public void testGetBooleanProperty() throws Exception {
        ELPropertyDescriptor propertyDescriptor =
            getAndCheckPropertyDescriptor(Bean.class, "readOnly", "isReadOnly", Boolean.TYPE, true, true);
    }

    @Test
    public void testGetContextProperty() throws Exception {
        ELPropertyDescriptor propertyDescriptor =
            getAndCheckPropertyDescriptor(Bean.class, "context", "getContext", FacesContext.class, true, false);
    }

    @Test
    public void testGetDummyProperty() throws Exception {
        ELPropertyDescriptor propertyDescriptor =
            getAndCheckPropertyDescriptor(Bean.class, "nonExistedProperty", "getNonExistedProperty", Object.class,
                false, false);
    }

    public ELType findAndCheckMethod(Class<?> clazz, String methodName, Class<?> expectedType, Class<?>... arguments)
        throws Exception {
        ELType beanType = typesFactory.getType(clazz);
        ELType[] parameters = new ELType[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            parameters[i] = typesFactory.getType(arguments[i]);
        }
        ELType returnType = typesFactory.getMatchingVisibleMethodReturnType(beanType, methodName, parameters);
        assertNotNull(returnType);
        assertEquals(expectedType.getName(), returnType.getRawName());
        return returnType;
    }

    @Test
    public void testFindCountMethod() throws Exception {
        findAndCheckMethod(Bean.class, "count", Integer.class, Integer.TYPE);
    }
    @Test
    public void testFindCountMethodWithWrongParameter() throws Exception {
        findAndCheckMethod(Bean.class, "count", Object.class, String.class);
    }
    
    @Test
    public void testFindToStringMethod() throws Exception {
        findAndCheckMethod(Bean.class, "toString", String.class);
    }

    @Test
    public void testFindCollectionMethod() throws Exception {
        ELType type = findAndCheckMethod(Bean.class, "getComponents", List.class);
        assertEquals(UIComponent.class.getName(), type.getContainerType().getRawName());
    }
}
