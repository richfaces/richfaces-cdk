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
package org.richfaces.cdk.attributes;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Nick Belaevski
 * 
 */
public final class Adapters {

    private abstract static class XmlTypeAdapter<KeyType extends KeyedType, ValueType extends ContainerType<KeyType>>
        extends XmlAdapter<ValueType, Map<String, KeyType>> {

        private Class<? extends ValueType> valueTypeClass;

        public XmlTypeAdapter(Class<? extends ValueType> valueTypeClass) {
            super();
            this.valueTypeClass = valueTypeClass;
        }

        @Override
        public ValueType marshal(Map<String, KeyType> v) throws Exception {
            ValueType valueType = valueTypeClass.newInstance();
            valueType.setChildren(v.values());
            return valueType;
        }

        @Override
        public Map<String, KeyType> unmarshal(ValueType v) throws Exception {
            Map<String, KeyType> result = new TreeMap<String, KeyType>();

            Collection<? extends KeyType> items = v.getChildren();
            for (KeyType keyedType : items) {
                result.put(keyedType.getKey(), keyedType);
            }

            return result;
        }
    }

    public static final class SchemaAdapter extends XmlTypeAdapter<Schema, Schema.Type> {

        public SchemaAdapter() {
            super(Schema.Type.class);
        }

    }

    public static final class ElementAdapter extends XmlTypeAdapter<Element, Element.Type> {

        public ElementAdapter() {
            super(Element.Type.class);
        }

    }

    public static final class AttributeAdapter extends XmlTypeAdapter<Attribute, Attribute.Type> {

        public AttributeAdapter() {
            super(Attribute.Type.class);
        }

    }
    
    public static final class NormalizedStringAdapter extends XmlAdapter<String, String> {

        @Override
        public String marshal(String text) {
            return text.trim();
        }

        @Override
        public String unmarshal(String v) throws Exception {
            return v.trim();
        }
    }
    
    private Adapters() {
    }
}
