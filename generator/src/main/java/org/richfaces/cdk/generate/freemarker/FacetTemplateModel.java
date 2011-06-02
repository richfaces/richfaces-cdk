/*
 * $Id: PropertyModel.java 21464 2011-02-04 18:31:43Z alexsmirnov $
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

import org.richfaces.cdk.model.FacetModel;
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
public class FacetTemplateModel extends StringModel implements TemplateModel {
    static final ModelFactory FACTORY = new ModelFactory() {
        public TemplateModel create(Object object, ObjectWrapper wrapper) {
            return new FacetTemplateModel((FacetModel) object, (BeansWrapper) wrapper);
        }
    };
    private final FacetModel facet;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param object
     * @param wrapper
     */
    public FacetTemplateModel(FacetModel object, BeansWrapper wrapper) {
        super(object, wrapper);
        facet = object;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public String getAsString() {
        return facet.getName();
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
        } else {
            return super.get(key);
        }

        return wrapper.wrap(value);
    }

    // Model utility methods.
    public String getGetterName() {
        return "get" + capitalizeName();
    }

    public String getSetterName() {
        return "set" + capitalizeName();
    }

    public String capitalizeName() {
        return Strings.firstToUpperCase(getAsString());
    }
}
