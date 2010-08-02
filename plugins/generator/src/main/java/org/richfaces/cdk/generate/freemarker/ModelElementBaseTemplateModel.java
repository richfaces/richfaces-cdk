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

import java.util.NoSuchElementException;
import java.util.Set;

import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.ConverterModel;
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.ValidatorModel;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

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
public class ModelElementBaseTemplateModel extends StringModel implements TemplateModel {
    static final ModelFactory FACTORY = new ModelFactory() {
        public TemplateModel create(Object object, ObjectWrapper wrapper) {
            return new ModelElementBaseTemplateModel((ModelElementBase) object, (BeansWrapper) wrapper);
        }
    };

    private final ModelElementBase model;
    private Set<EventName> eventNames;
    private final String name;

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param object
     * @param wrapper
     */
    public ModelElementBaseTemplateModel(ModelElementBase object, BeansWrapper wrapper) {
        super(object, wrapper);
        model = object;
        if (model instanceof ComponentModel) {
            this.name = "Component";
        } else if (model instanceof BehaviorModel) {
            this.name = "Behavior";
        } else if (model instanceof ConverterModel) {
            this.name = "Converter";
        } else if (model instanceof ValidatorModel) {
            this.name = "Validator";
        } else {
            this.name = "UnknownModelElement";
        }
    }

    @Override
    public String getAsString() {
        return name;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        if ("requiredAttributes".equals(key)) {
            return requiredAttributes();
        } else if ("generatedAttributes".equals(key)) {
            return generatedAttributes();
        } else if ("tagAttributes".equals(key)) {
            return tagAttributes();
        } else if ("eventNames".equals(key)) {
            return eventNames();
        } else if ("defaultEvent".equals(key)) {
            return defaultEvent();
        } else if ("importClasses".equals(key)) {
            return getImportClasses();
        } else if ("implemented".equals(key)) {
            return getImplementedInterfaces();
        }

        return super.get(key);
    }

    public TemplateModel getImportClasses() throws TemplateModelException {
        Set<ClassName> result = Sets.newTreeSet();

        for (PropertyBase entry : model.getAttributes()) {
            if (entry.isGenerate() && !isPredefined(entry)) {
                result.add(entry.getType());
            }
        }
        // Import all interfaces implemented by the generated class.
        result.addAll(model.getInterfaces());
        if (model instanceof ComponentModel) {
            ComponentModel component = (ComponentModel) model;
            for(EventModel event :component.getEvents()){
                result.add(event.getSourceInterface());
                result.add(event.getListenerInterface());
            }
        }
        // Collection<String> list = new ArrayList<String>(result);
        return this.wrapper.wrap(result);
    }

    public TemplateModel getImplementedInterfaces() throws TemplateModelException {
        Set<ClassName> result = Sets.newTreeSet();
        if(getEventNames().size()>0){
            result.add(ClassName.parseName("javax.faces.component.behavior.ClientBehaviorHolder"));
        }
        // Import all interfaces implemented by the generated class.
        result.addAll(model.getInterfaces());
        if (model instanceof ComponentModel) {
            ComponentModel component = (ComponentModel) model;
            for(EventModel event :component.getEvents()){
                result.add(event.getSourceInterface());
            }
        }
        // Collection<String> list = new ArrayList<String>(result);
        return this.wrapper.wrap(result);
    }

    public boolean isPredefined(PropertyBase property) {
        return property.isPrimitive() || isFromJavaLang(property.getType());
    }

    public boolean isFromJavaLang(ClassName type) {
        return "java.lang".equals(type.getPackage());
    }

    private TemplateModel eventNames() throws TemplateModelException {
        return wrapper.wrap(getEventNames());
    }

    private TemplateModel defaultEvent() throws TemplateModelException {
        Set<EventName> names = getEventNames();

        try {
            EventName defaultEvent = Iterables.find(names, new Predicate<EventName>() {
                @Override
                public boolean apply(EventName event) {
                    return event.isDefaultEvent();
                }
            });

            return wrapper.wrap(defaultEvent);
        } catch (NoSuchElementException e) {
            return wrapper.wrap(null);
        }
    }

    private Set<EventName> getEventNames() {
        if (null == eventNames) {
            eventNames = Sets.newHashSet();

            for (PropertyBase property : model.getAttributes()) {
                eventNames.addAll(property.getEventNames());
            }
        }

        return eventNames;
    }

    private TemplateModel requiredAttributes() throws TemplateModelException {
        return wrapper.wrap(Collections2.filter(model.getAttributes(), new Predicate<PropertyBase>() {

            @Override
            public boolean apply(PropertyBase input) {
                return input.isRequired();
            }
        }));
    }

    private TemplateModel generatedAttributes() throws TemplateModelException {
        return wrapper.wrap(Collections2.filter(model.getAttributes(), new Predicate<PropertyBase>() {

            @Override
            public boolean apply(PropertyBase input) {
                return input.isGenerate();
            }
        }));
    }

    private TemplateModel tagAttributes() throws TemplateModelException {
        return wrapper.wrap(Collections2.filter(model.getAttributes(), new Predicate<PropertyBase>() {

            @Override
            public boolean apply(PropertyBase input) {
                return !(input.isHidden()||input.isReadOnly());
            }
        }));
    }

}
