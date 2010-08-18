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
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Nick Belaevski
 * 
 */
public class Schema implements KeyedType {

    @XmlRootElement(name = "schemas")
    @XmlType(name = "SchemasType")
    public static final class Type implements ContainerType<Schema> {

        private Collection<Schema> children = new HashSet<Schema>();

        @XmlElement(name = "schema")
        public Collection<Schema> getChildren() {
            return children;
        }
        
        @Override
        public void setChildren(Collection<Schema> values) {
            this.children = values;
        }
    }

    private String namespace;

    private Map<String, Element> elements = new TreeMap<String, Element>();
    
    public Schema() {
        super();
    }
    
    public Schema(String namespace) {
        super();
        this.namespace = namespace;
    }

    /**
     * @return the namespace
     */
    @XmlElement
    @XmlJavaTypeAdapter(Adapters.NormalizedStringAdapter.class)
    public String getNamespace() {
        return namespace;
    }
    
    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    @Override
    public String getKey() {
        return getNamespace();
    }
    
    public void addElement(Element element) {
        elements.put(element.getName(), element);
    }
    
    /**
     * @return the elements
     */
    @XmlJavaTypeAdapter(Adapters.ElementAdapter.class)
    public Map<String, Element> getElements() {
        return elements;
    }
    
    /**
     * @param elements the elements to set
     */
    public void setElements(Map<String, Element> elements) {
        this.elements = elements;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        Schema other = (Schema) obj;
        if (namespace == null) {
            if (other.namespace != null) {
                return false;
            }
        } else if (!namespace.equals(other.namespace)) {
            return false;
        }
        return true;
    }

}
