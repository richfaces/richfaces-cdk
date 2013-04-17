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

import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;

/**
 * @author Nick Belaevski
 *
 */
public class ReferencedType implements ELType {
    private String classCodeString;

    public ReferencedType(String classCodeString) {
        super();

        this.classCodeString = classCodeString;
    }

    String getClassCodeString() {
        return classCodeString;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getCode()
     */
    @Override
    public String getCode() {
        return classCodeString;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getImportsList()
     */
    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return Collections.emptyList();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getRawType()
     */
    @Override
    public ELType getRawType() {
        return TypesFactory.OBJECT_TYPE;
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
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getTypeArguments()
     */
    @Override
    public ELType[] getTypeArguments() {
        return PlainClassType.NO_TYPES;
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
        result = prime * result + ((classCodeString == null) ? 0 : classCodeString.hashCode());
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
        ReferencedType other = (ReferencedType) obj;
        if (classCodeString == null) {
            if (other.classCodeString != null) {
                return false;
            }
        } else if (!classCodeString.equals(other.classCodeString)) {
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
        return MessageFormat.format("{0}: {1}", getClass().getName(), getCode());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.ELType#getCompositeType()
     */
    @Override
    public ELType getContainerType() {
        return null;
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
        if (anotherType instanceof ReferencedType) {
            ReferencedType anotherReferencedType = (ReferencedType) anotherType;

            return getClassCodeString().equals(anotherReferencedType.getClassCodeString());
        }

        return false;
    }

    @Override
    public String getRawName() {
        return getClassCodeString();
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }
}
