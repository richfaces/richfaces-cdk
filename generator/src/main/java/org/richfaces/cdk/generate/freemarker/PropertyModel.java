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
package org.richfaces.cdk.generate.freemarker;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.util.Strings;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class PropertyModel extends StringModel implements TemplateModel {
    static final ModelFactory FACTORY = new ModelFactory() {
        public TemplateModel create(Object object, ObjectWrapper wrapper) {
            return new PropertyModel((PropertyBase) object, (BeansWrapper) wrapper);
        }
    };
    private final PropertyBase property;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param object
     * @param wrapper
     */
    public PropertyModel(PropertyBase object, BeansWrapper wrapper) {
        super(object, wrapper);
        property = object;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public String getAsString() {
        return property.getName();
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        Object value;

        if ("getterName".equals(key)) {
            value = getGetterName();
        } else if ("setterName".equals(key)) {
            value = getSetterName();
        } else if ("name".equals(key)) {
            value = getAsString();
        } else if ("capitalizeName".equals(key)) {
            value = capitalizeName();
        } else if ("typeForCasting".equals(key)) {
            value = typeForCasting();
        } else if ("isBingingAttribute".equals(key)) {
            value = isBingingAttribute();
        } else if ("isBinging".equals(key)) {
            value = isBinging();
        } else if ("typeName".equals(key)) {
            value = getTypeName();
        } else if ("isPrimitive".equals(key)) {
            value = isPrimitive();
        } else {
            return super.get(key);
        }

        return wrapper.wrap(value);
    }

    public boolean isBingingAttribute() {
        return this.property.isBindingAttribute();
    }

    public boolean isBinging() {
        return this.property.isBinding();
    }

    public boolean isPrimitive() {
        return this.property.isPrimitive();
    }

    private String typeForCasting() {
        ClassName type = this.property.getType();
        return type.getSimpleBoxingName();
    }

    // Model utility methods.
    public String getGetterName() {
        return property.getType().getGetterPrefix() + capitalizeName();
    }

    public String getSetterName() {
        return "set" + capitalizeName();
    }

    public String capitalizeName() {
        return Strings.firstToUpperCase(getAsString());
    }

    public boolean isFromJavaLang(ClassName type) {
        return "java.lang".equals(type.getPackage());
    }

    public String getTypeName() {
        ClassName type = this.property.getType();
        return type.getSimpleName();
    }
}
