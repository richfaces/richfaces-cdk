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
package org.richfaces.cdk.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This is the base class for all Java Bean-like JSF objects in the model.
 *
 * @author akolonitsky
 * @since Jan 22, 2010
 *
 */
public class BeanModelBase extends DescriptionGroupBase {
    private final ModelCollection<PropertyBase> attributes = ModelSet.<PropertyBase>create(new PropertyBaseComparator());

    private static class PropertyBaseComparator implements Comparator<PropertyBase>, Serializable {
        @Override
        public int compare(PropertyBase o1, PropertyBase o2) {
            if (o1 != null && o2 != null && null != o1.getName() && null != o2.getName()) {
                return o1.getName().compareTo(o2.getName());
            } else {
                return 0;
            }
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Represents JSF component attributes and properties.
     * </p>
     *
     * @return the attributes
     */
    public ModelCollection<PropertyBase> getAttributes() {
        return attributes;
    }

    /**
     * <p class="changed_added_4_0">
     * Return bean attribute for given name.
     * </p>
     *
     * @param name
     * @return
     */
    public PropertyBase getAttribute(final String name) {
        return attributes.find(new Named.NamePredicate(name));
    }

    public PropertyBase getOrCreateAttribute(String attributeName) {
        PropertyBase attribute = getAttribute(attributeName);
        if (null == attribute) {
            attribute = createAttribute();
            attribute.setName(attributeName);
            attributes.add(attribute);
        }

        return attribute;
    }

    protected PropertyBase createAttribute() {
        return new PropertyModel();
    }
}
