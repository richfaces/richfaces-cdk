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
package org.richfaces.cdk.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.NoSuchElementException;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.model.InvalidNameException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public final class PropertyUtils {
    private PropertyUtils() {
    }

    public static String methodToName(String methodName) throws InvalidNameException {
        if (null != methodName) {
            if (methodName.length() > 3 && Character.isUpperCase(methodName.charAt(3))
                    && (methodName.startsWith("set") || methodName.startsWith("get"))) {
                return Strings.firstToLowerCase(methodName.substring(3));
            } else if (methodName.length() > 2 && Character.isUpperCase(methodName.charAt(2)) && methodName.startsWith("is")) {
                return Strings.firstToLowerCase(methodName.substring(2));
            }
        }

        throw new InvalidNameException("Method name " + methodName + " is not valid bean property getter or setter");
    }

    public static void setPropertyValue(Object bean, String propertyName, Object newValue) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean, propertyName);
        if (null != propertyDescriptor.getWriteMethod()) {
            try {
                propertyDescriptor.getWriteMethod().invoke(bean, newValue);
            } catch (Exception e) {
                throw new CdkException("Cannot set new value for bean property " + bean.getClass().getName() + "#"
                        + propertyName, e);
            }
        } else {
            throw new CdkException("Bean property not writable " + bean.getClass().getName() + "#" + propertyName);
        }
    }

    public static PropertyDescriptor getPropertyDescriptor(Object bean, String propertyName) {
        PropertyDescriptor[] properties;
        try {
            properties = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new CdkException(e);
        }
        for (PropertyDescriptor propertyDescriptor : properties) {
            if (propertyName.equals(propertyDescriptor.getName())) {
                return propertyDescriptor;
            }
        }
        throw new NoSuchElementException("Bean property " + propertyName + " not found in class " + bean.getClass().getName());
    }
}
