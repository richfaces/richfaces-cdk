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
package org.richfaces.cdk.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.xmlconfig.JAXBBinding;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

/**
 * @author Nick Belaevski
 */
public final class JavaUtils {
    public static final ImmutableMap<Class<?>, Class<?>> BOXING_CLASSES = ImmutableMap.<Class<?>, Class<?>>builder()
            .put(Boolean.TYPE, Boolean.class).put(Character.TYPE, Character.class).put(Byte.TYPE, Byte.class)
            .put(Short.TYPE, Short.class).put(Integer.TYPE, Integer.class).put(Long.TYPE, Long.class)
            .put(Float.TYPE, Float.class).put(Double.TYPE, Double.class).build();
    public static final Function<Class<?>, String> CLASS_TO_CLASS_NAME = new Function<Class<?>, String>() {
        public String apply(java.lang.Class<?> from) {
            return from.getName();
        }
    };

    private JavaUtils() {
        // private constructor
    }

    /**
     * <p class="changed_added_4_0">
     * Transform character to Java code expression
     * </p>
     *
     * @param c
     * @return
     */
    private static String toCharExpression(char c) {
        String prependingZeroesString;
        String hexString = Integer.toHexString(c);

        switch (hexString.length()) {
            case 1:
                prependingZeroesString = "000";
                break;
            case 2:
                prependingZeroesString = "00";
                break;
            case 3:
                prependingZeroesString = "0";
                break;
            case 4:
                prependingZeroesString = "";
                break;
            default:
                throw new IllegalArgumentException();
        }

        return "\\u" + prependingZeroesString + hexString.toUpperCase(Locale.US);
    }

    /**
     * <p class="changed_added_4_0">
     * Escapes string into Java language expression
     * </p>
     *
     * @param s
     * @return
     */
    public static String getEscapedString(String s) {
        StringBuilder result = new StringBuilder();
        result.append('"');

        char[] chars = s.toCharArray();

        for (char c : chars) {
            if (c == '\n') {
                result.append("\\n");
            } else if (c == '\r') {
                result.append("\\r");
            } else if (c == '\t') {
                result.append("\\t");
            } else if (c == '\f') {
                result.append("\\f");
            } else if (c == '\b') {
                result.append("\\b");
            } else if (c == '\\') {
                result.append("\\\\");
            } else if (c == '"') {
                result.append("\\\"");
            } else {
                if (c < 0x20 || c > 0x7F) {
                    result.append(toCharExpression(c));
                } else {
                    result.append(c);
                }
            }
        }

        result.append('"');

        return result.toString();
    }

    /**
     * <p class="changed_added_4_0">
     * Escapes sequence of strings into comma-separated sequence of Java language expressions
     * </p>
     *
     * @param strings
     * @return
     */
    public static String getEscapedStringsArray(Iterable<String> strings) {
        StringBuilder sb = new StringBuilder();

        if (strings != null) {
            for (String string : strings) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }

                sb.append(getEscapedString(string));
            }
        }

        return sb.toString();
    }

    /**
     * Returns true if the char isalpha() or isdigit().
     */
    public static boolean isalnum(char c) {
        return isalpha(c) || isdigit(c);
    }

    /**
     * Returns true if the char isupper() or islower().
     */
    public static boolean isalpha(char c) {
        return isupper(c) || islower(c);
    }

    /**
     * Returns true if the char is from 'A' to 'Z' inclusive.
     */
    public static boolean isupper(char c) {
        return (c >= 'A') && (c <= 'Z');
    }

    /**
     * Returns true if the char is from 'a' to 'z' inclusive.
     */
    public static boolean islower(char c) {
        return (c >= 'a') && (c <= 'z');
    }

    /**
     * Returns true if the char is from '0' to '9' inclusive.
     */
    public static boolean isdigit(char c) {
        return (c >= '0') && (c <= '9');
    }

    public static Class<?> toBoxingClass(Class<?> targetType) {
        if (targetType.isPrimitive()) {
            return BOXING_CLASSES.get(targetType);
        }
        return targetType;
    }

    public static void copyProperties(Object source, Object destination) throws CdkException {
        try {
            PropertyDescriptor[] targetProperties = Introspector.getBeanInfo(destination.getClass()).getPropertyDescriptors();
            PropertyDescriptor[] sourceProperties = Introspector.getBeanInfo(source.getClass()).getPropertyDescriptors();

            for (PropertyDescriptor targetProperty : targetProperties) {
                String name = targetProperty.getName();

                for (PropertyDescriptor sourceProperty : sourceProperties) {
                    if (!("class".equals(name) || "extension".equals(name)) && name.equals(sourceProperty.getName())) {
                        try {
                            writeProperty(source, destination, targetProperty, sourceProperty);
                        } catch (Exception e) {
                            throw new CdkException("Error on copying property " + name + " from object " + source.getClass()
                                    + " to " + destination.getClass(), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new CdkException("Properties copying error", e);
        }
    }

    public static void writeProperty(Object source, Object destination, PropertyDescriptor targetProperty,
            PropertyDescriptor sourceProperty) throws IllegalAccessException, InvocationTargetException {
        Method readMethod = sourceProperty.getReadMethod();
        if (readMethod == null) {
            return;
        }

        Object propertyValue = readMethod.invoke(source);
        if (propertyValue != null) {
            Class<?> targetType = targetProperty.getPropertyType();
            targetType = toBoxingClass(targetType);
            Class<?> sourceType = sourceProperty.getPropertyType();
            sourceType = toBoxingClass(sourceType);
            Method writeMethod = targetProperty.getWriteMethod();
            if (JAXBBinding.isCollections(targetType, propertyValue)) {
                Collection targetCollection = (Collection) targetProperty.getReadMethod().invoke(destination);

                if (null != targetCollection) {
                    targetCollection.addAll((Collection) propertyValue);
                } else if (writeMethod != null) {
                    writeMethod.invoke(destination, propertyValue);
                }
            } else if (writeMethod != null) {
                if (targetType.equals(sourceType)) {
                    writeMethod.invoke(destination, propertyValue);
                } else if (targetType.equals(String.class)) {
                    writeMethod.invoke(destination, propertyValue.toString());
                }
            }
        }
    }
}
