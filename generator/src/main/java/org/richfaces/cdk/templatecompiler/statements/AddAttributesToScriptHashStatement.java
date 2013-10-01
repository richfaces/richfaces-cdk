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
package org.richfaces.cdk.templatecompiler.statements;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.namespace.QName;

import org.richfaces.cdk.attributes.Attribute.Kind;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.Named.NamePredicate;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.templatecompiler.TemplateModel;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 */
public class AddAttributesToScriptHashStatement extends FreeMarkerTemplateStatementBase {
    /**
     *
     */
    private static final String ATTRIBUTES_FIELD_NAME = "ATTRIBUTES_FOR_SCRIPT_HASH";
    private static AtomicInteger fieldCounter = new AtomicInteger(0);
    private String fieldName;
    private String wrapper;
    private Collection<PassThrough> attributes = Lists.newArrayList();
    private Collection<PropertyBase> componentAttributes;

    @Inject
    public AddAttributesToScriptHashStatement(@TemplateModel FreeMarkerRenderer renderer) {
        super(renderer, "add-attributes-to-script-hash");
        fieldName = ATTRIBUTES_FIELD_NAME + fieldCounter.getAndIncrement();
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the attributes
     */
    public Collection<PassThrough> getAttributes() {
        return this.attributes;
    }

    /**
     * @return the wrapper
     */
    public String getWrapper() {
        return wrapper;
    }

    /**
     * @param wrapper the wrapper to set
     */
    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * @param attributeNames
     * @param attributes
     */
    public void setAttributes(Collection<String> attributeNames, Collection<PropertyBase> componentAttributes) {
        this.componentAttributes = componentAttributes;

        for (String attributeName : attributeNames) {
            String[] splitAttr = attributeName.split(":");
            String htmlAttributeName = splitAttr[0];
            String componentAttributeName = splitAttr.length > 1 ? splitAttr[1] : htmlAttributeName;
            PassThrough passThrough = new PassThrough();
            passThrough.name = QName.valueOf(htmlAttributeName);
            passThrough.componentAttribute = componentAttributeName;

            try {
                PropertyBase componentAttribute = findComponentAttribute(componentAttributeName);
                for (EventName event : componentAttribute.getEventNames()) {
                    passThrough.behaviors.add(event.getName());
                }
                passThrough.defaultValue = componentAttribute.getDefaultValue();
                passThrough.type = componentAttribute.getType().getSimpleName();
            } catch (NoSuchElementException e) {
                passThrough.type = Object.class.getName();
            }

            if (Boolean.TYPE.equals(passThrough.type) || Boolean.class.equals(passThrough.type)) {
                passThrough.kind = Kind.BOOLEAN;
            } else {
                passThrough.kind = Kind.GENERIC;
            }

            attributes.add(passThrough);
        }
    }

    private PropertyBase findComponentAttribute(final String name) throws NoSuchElementException {

        return Iterables.find(componentAttributes, new NamePredicate(name));
    }
}
