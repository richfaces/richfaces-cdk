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
public class Element implements KeyedType {

    @XmlRootElement(name = "elements")
    @XmlType(name = "ElementsType")
    public static final class Type implements ContainerType<Element> {

        private Collection<Element> children = new HashSet<Element>();

        @XmlElement(name = "element")
        public Collection<Element> getChildren() {
            return children;
        }
        
        @Override
        public void setChildren(Collection<Element> values) {
            this.children = values;
        }
    }

    private String name;
    
    private Map<String, Attribute> attributes = new TreeMap<String, Attribute>();

    public Element() {
        super();
    }
    
    public Element(String name) {
        super();
        this.name = name;
    }

    /**
     * @return the name
     */
    @XmlElement
    @XmlJavaTypeAdapter(Adapters.NormalizedStringAdapter.class)
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getKey() {
        return getName();
    }
    
    public void addAttribute(Attribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }
    
    /**
     * @return the attributes
     */
    @XmlJavaTypeAdapter(Adapters.AttributeAdapter.class)
    public Map<String, Attribute> getAttributes() {
        return attributes;
    }
    
    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Element other = (Element) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
