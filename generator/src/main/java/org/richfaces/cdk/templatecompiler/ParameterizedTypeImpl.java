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
package org.richfaces.cdk.templatecompiler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author Nick Belaevski
 *
 */
class ParameterizedTypeImpl implements ParameterizedType {
    private Type rawType;
    private Type[] actualTypeArguments;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments) {
        super();
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.reflect.ParameterizedType#getActualTypeArguments()
     */
    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.reflect.ParameterizedType#getOwnerType()
     */
    @Override
    public Type getOwnerType() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.reflect.ParameterizedType#getRawType()
     */
    @Override
    public Type getRawType() {
        return rawType;
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
        result = prime * result + Arrays.hashCode(actualTypeArguments);
        result = prime * result + ((rawType == null) ? 0 : rawType.hashCode());
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
        ParameterizedTypeImpl other = (ParameterizedTypeImpl) obj;
        if (!Arrays.equals(actualTypeArguments, other.actualTypeArguments)) {
            return false;
        }
        if (rawType == null) {
            if (other.rawType != null) {
                return false;
            }
        } else if (!rawType.equals(other.rawType)) {
            return false;
        }
        return true;
    }
}
