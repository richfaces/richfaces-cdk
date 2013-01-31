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

import java.io.Serializable;

import org.richfaces.cdk.util.Strings;

import com.google.common.collect.ImmutableMap;

/**
 * <p class="changed_added_4_0">
 * Tthat class represents information about Jsf object class.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class ClassName implements Serializable, Comparable<ClassName> {
    private static final long serialVersionUID = -846623207703750456L;
    private static final ImmutableMap<String, String> PRIMITIVE_TYPES = ImmutableMap.<String, String>builder()
            .put(boolean.class.getName(), Boolean.class.getName()).put(byte.class.getName(), Byte.class.getName())
            .put(char.class.getName(), Character.class.getName()).put(short.class.getName(), Short.class.getName())
            .put(int.class.getName(), Integer.class.getName()).put(long.class.getName(), Long.class.getName())
            .put(float.class.getName(), Float.class.getName()).put(double.class.getName(), Double.class.getName()).build();
    private static final ImmutableMap<String, String> DEFAULT_VALUES = ImmutableMap.<String, String>builder()
            .put(boolean.class.getName(), "Boolean.FALSE").put(byte.class.getName(), "Byte.MIN_VALUE")
            .put(char.class.getName(), "Character.MIN_VALUE").put(short.class.getName(), "Short.MIN_VALUE")
            .put(int.class.getName(), "Integer.MIN_VALUE").put(long.class.getName(), "Long.MIN_VALUE")
            .put(float.class.getName(), "Float.MIN_VALUE").put(double.class.getName(), "Double.MIN_VALUE").build();
    private final String boxingClassName;
    private final String fullName;
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private final String name;
    private final boolean primitive;
    /**
     * <p class="changed_added_4_0">
     * Id parameters for that class
     * </p>
     * TODO append type parameters to key.
     */
    private String typeParameters;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param cl
     */
    public ClassName(Class<?> cl) {
        this(cl.getName().replace('$', '.'));
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param name
     */
    public ClassName(String name) {
        fullName = name;

        if (PRIMITIVE_TYPES.containsKey(name)) {
            this.name = name;
            boxingClassName = PRIMITIVE_TYPES.get(name);
            primitive = true;
        } else {
            int i = name.indexOf('<');

            if (i > 0) {
                int closeBracket = name.lastIndexOf('>');
                if (closeBracket < 0 || closeBracket == name.length() - 1) {
                    this.name = name.substring(0, i);
                    this.typeParameters = name.substring(i);
                } else {
                    this.name = name.substring(0, i) + name.substring(closeBracket + 1);
                    this.typeParameters = name.substring(i, closeBracket + 1);
                }
            } else {
                this.name = name;
            }

            boxingClassName = name;
            primitive = false;
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Factory method to create class names. For empty or null name, returns null
     * </p>
     *
     * @param name fully-cvalified class name
     * @return new class name instance or null.
     */
    public static ClassName parseName(String name) {
        return Strings.isEmpty(name) ? null : new ClassName(name);
    }

    public static ClassName get(String name) {
        if (name == null) {
            return null;
        }

        return new ClassName(name);
    }

    public static ClassName get(Class name) {
        if (name == null) {
            return null;
        }

        return new ClassName(name);
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the typeParameters
     */
    public String getTypeParameters() {
        return typeParameters;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param typeParameters the typeParameters to set
     */
    public void setTypeParameters(String typeParameters) {
        this.typeParameters = typeParameters;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return package name.
     */
    public String getPackage() {
        int indexOfPeriod = name.lastIndexOf('.');

        if (indexOfPeriod > 0) {
            return name.substring(0, indexOfPeriod);
        } else {
            return null;
        }
    }

    public String getSimpleName() {
        int indexOfPeriod = name.lastIndexOf('.');

        if (indexOfPeriod > 0) {
            return fullName.substring(indexOfPeriod + 1);
        } else {
            return fullName;
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Return simple name for boxing class: Booilean,Integer etc.
     * </p>
     *
     * @return
     */
    public String getSimpleBoxingName() {
        if (isPrimitive()) {
            int indexOfPeriod;
            indexOfPeriod = boxingClassName.lastIndexOf('.');
            if (indexOfPeriod > 0) {
                return boxingClassName.substring(indexOfPeriod + 1);
            } else {
                return boxingClassName;
            }
        } else {
            return getSimpleName();
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the primitive
     */
    public boolean isPrimitive() {
        return primitive;
    }

    public String getDefaultValue() {
        return DEFAULT_VALUES.get(name);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the boxingClassName
     */
    public String getBoxingName() {
        return boxingClassName;
    }

    public String getGetterPrefix() {
        return primitive && Boolean.class.getName().equals(boxingClassName) ? "is" : "get";
    }

    @Override
    public String toString() {
        return fullName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((name == null) ? 0 : name.hashCode());

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ClassName)) {
            return false;
        }

        ClassName other = (ClassName) obj;

        if (fullName == null) {
            if (other.fullName != null) {
                return false;
            }
        } else if (!fullName.equals(other.fullName)) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(ClassName o) {
        return getName().compareTo(o.getName());
    }
}
