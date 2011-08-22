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
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.FacetModel;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.TagModel;

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
public class LibraryModelWrapper extends BeansWrapper implements ObjectWrapper {
    static final ModelFactory STRING_MODEL_FACTORY = new ModelFactory() {
        public TemplateModel create(Object object, ObjectWrapper wrapper) {
            return new StringModel(object, (BeansWrapper) wrapper);
        }
    };

    public LibraryModelWrapper() {
        super();
        // setStrict(true);
        setSimpleMapWrapper(true);
        // setNullModel(TemplateScalarModel.EMPTY_STRING);
        setUseCache(true);
    }

    @Override
    public TemplateModel wrap(Object obj) throws TemplateModelException {
        if (obj instanceof PropertyBase) {
            return create(obj, PropertyModel.FACTORY);
        } else if (obj instanceof ClassName) {
            return create(obj, ClassNameModel.FACTORY);
        } else if (obj instanceof FacesId) {
            return create(obj, STRING_MODEL_FACTORY);
        } else if (obj instanceof EventModel) {
            return create(obj, EventTemplateModel.FACTORY);
        } else if (obj instanceof TagModel) {
            return create(obj, TagTemplateModel.FACTORY);
        } else if (obj instanceof FacetModel) {
            return create(obj, FacetTemplateModel.FACTORY);
        } else if (obj instanceof ModelElementBase) {
            return create(obj, ModelElementBaseTemplateModel.FACTORY);
        } else {
            return super.wrap(obj);
        }
    }
}
