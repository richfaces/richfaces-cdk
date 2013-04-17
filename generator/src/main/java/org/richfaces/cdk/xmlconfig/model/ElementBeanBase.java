/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.xmlconfig.model;

import java.util.Collection;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ConfigExtension;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.PropertyModel;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * TODO - is the tho different collections are necessary ?
 *
 * @author akolonitsky
 * @since Mar 19, 2010
 */
@SuppressWarnings("unchecked")
public abstract class ElementBeanBase<E extends ConfigExtension> extends ExtensibleBean<E> {
    private static final Predicate<PropertyBase> PROPERTY_PREDICATE = new Predicate<PropertyBase>() {
        @Override
        public boolean apply(PropertyBase input) {
            return input instanceof PropertyModel;
        }
    };
    private static final Predicate<PropertyBase> ATTRIBUTE_PREDICATE = new Predicate<PropertyBase>() {
        @Override
        public boolean apply(PropertyBase input) {
            return input instanceof AttributeModel;
        }
    };
    private static final Predicate<PropertyBase> VISIBLE_PROPERTY_PREDICATE = new Predicate<PropertyBase>() {
        @Override
        public boolean apply(PropertyBase input) {
            if (input instanceof PropertyModel) {
                return !input.isHidden();
            }
            return false;
        }
    };
    private static final Predicate<PropertyBase> VISIBLE_ATTRIBUTE_PREDICATE = new Predicate<PropertyBase>() {
        @Override
        public boolean apply(PropertyBase input) {
            if (input instanceof AttributeModel) {
                return !input.isHidden();
            }
            return false;
        }
    };
    private Collection<? extends PropertyBase> attributes = Lists.newArrayList();
    private Collection<PropertyModel> properties = (Collection<PropertyModel>) Collections2.filter(attributes,
            PROPERTY_PREDICATE);
    private Collection<AttributeModel> facesAttributes = (Collection<AttributeModel>) Collections2.filter(attributes,
            ATTRIBUTE_PREDICATE);

    public Collection<PropertyModel> getProperties() {
        return properties;
    }

    public Collection<AttributeModel> getFacesAttributes() {
        return this.facesAttributes;
    }

    public Collection<PropertyBase> getAttributes() {
        return (Collection<PropertyBase>) attributes;
    }

    /**
     * <p class="changed_added_4_0">
     * Set filtering visible properties and attributes, to unmarshall public properties only.
     * </p>
     *
     * @param filter
     */
    public void setFilterHiddenAttributes(boolean filter) {
        if (filter) {
            properties = (Collection<PropertyModel>) Collections2.filter(this.attributes, VISIBLE_PROPERTY_PREDICATE);
            facesAttributes = (Collection<AttributeModel>) Collections2.filter(this.attributes, VISIBLE_ATTRIBUTE_PREDICATE);
        } else {
            properties = (Collection<PropertyModel>) Collections2.filter(this.attributes, PROPERTY_PREDICATE);
            facesAttributes = (Collection<AttributeModel>) Collections2.filter(this.attributes, ATTRIBUTE_PREDICATE);
        }
    }

    // public void setAttributes(Collection<PropertyBase> attributes) {
    // this.attributes = attributes;
    // properties = (Collection<PropertyModel>) Collections2.filter(this.attributes, PROPERTY_PREDICATE);
    // facesAttributes = (Collection<AttributeModel>) Collections2.filter(this.attributes, ATTRIBUTE_PREDICATE);
    // }
}
