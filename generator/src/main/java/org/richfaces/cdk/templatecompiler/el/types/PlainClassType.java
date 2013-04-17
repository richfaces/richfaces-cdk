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

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImportImpl;

/**
 * @author Nick Belaevski
 *
 */
public final class PlainClassType implements ELType {
    public static final ELType[] NO_TYPES = new ELType[0];
    private final Class<?> clazz;
    private final Set<JavaImport> imports;

    public PlainClassType(Class<?> clazz) {
        super();

        if (clazz.isArray()) {
            throw new IllegalArgumentException("Array classes are not supported");
        }

        this.clazz = clazz;
        this.imports = Collections.<JavaImport>singleton(new JavaImportImpl(clazz));
    }

    public Class<?> getPlainJavaClass() {
        return this.clazz;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getCode()
     */
    @Override
    public String getCode() {
        return clazz.getSimpleName();
    }

    @Override
    public String getRawName() {
        return clazz.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getImportsIterator()
     */
    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return imports;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#isNullType()
     */
    @Override
    public boolean isNullType() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getRawType()
     */
    @Override
    public ELType getRawType() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getTypeArguments()
     */
    @Override
    public ELType[] getTypeArguments() {
        return NO_TYPES;
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
        result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        PlainClassType other = (PlainClassType) obj;
        if (clazz == null) {
            if (other.clazz != null) {
                return false;
            }
        } else if (!clazz.equals(other.clazz)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return MessageFormat.format("{0}: {1}", getClass().getName(), clazz.toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getCompositeType()
     */
    @Override
    public ELType getContainerType() {
        return TypesFactory.OBJECT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#isArray()
     */
    @Override
    public boolean isArray() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#isAssignableFrom(org.richfaces.cdk.templatecompiler.el.ELType)
     */
    @Override
    public boolean isAssignableFrom(ELType anotherType) {
        if (Object.class.equals(clazz)) {
            return true;
        } else if (anotherType.isNullType()) {
            return !clazz.isPrimitive();
        } else if (anotherType instanceof PlainClassType) {
            Class<?> boxedClass = clazz.isPrimitive() ? TypesFactoryImpl.PRIMITIVE_TO_WRAPPER_CLASSES_MAP.get(clazz) : clazz;
            Class<?> clazz2 = ((PlainClassType) anotherType).clazz;
            clazz2 = clazz2.isPrimitive() ? TypesFactoryImpl.PRIMITIVE_TO_WRAPPER_CLASSES_MAP.get(clazz2) : clazz2;
            return boxedClass.isAssignableFrom(clazz2);
        } else {
            return getRawName().equals(anotherType.getRawName());
        }
    }

    @Override
    public boolean isPrimitive() {
        return clazz.isPrimitive();
    }
}
