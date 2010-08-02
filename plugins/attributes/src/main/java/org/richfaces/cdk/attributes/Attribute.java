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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Nick Belaevski
 * 
 */
public class Attribute implements KeyedType {

    public enum Kind {
        GENERIC, BOOLEAN, URI 
    }
    
    @XmlRootElement(name = "attributes")
    @XmlType(name = "AttributesType")
    public static final class Type implements ContainerType<Attribute> {

        private Collection<Attribute> children = new HashSet<Attribute>();

        @XmlElement(name = "attribute")
        public Collection<Attribute> getChildren() {
            return children;
        }
        
        @Override
        public void setChildren(Collection<Attribute> values) {
            this.children = values;
        }
    }

    private String name;
    
    private boolean required;

    private String defaultValue;
    
    private String componentAttributeName;
    
    private Kind kind = Kind.GENERIC;
    
    public Attribute() {
        super();
    }
    
    public Attribute(String name) {
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
    
    /**
     * @return the required
     */
    @XmlElement
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return the defaultValue
     */
    @XmlElement(name = "default-value")
    @XmlJavaTypeAdapter(Adapters.NormalizedStringAdapter.class)
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the kind
     */
    @XmlElement
    public Kind getKind() {
        return kind;
    }
    
    /**
     * @param kind the kind to set
     */
    public void setKind(Kind kind) {
        this.kind = kind;
    }

    /**
     * @return the componentAttributeName
     */
    @XmlElement(name = "component-attribute-name")
    @XmlJavaTypeAdapter(Adapters.NormalizedStringAdapter.class)
    public String getComponentAttributeName() {
        return componentAttributeName != null ? componentAttributeName : name;
    }
    
    /**
     * @param componentAttributeName the componentAttributeName to set
     */
    public void setComponentAttributeName(String componentAttributeName) {
        this.componentAttributeName = componentAttributeName;
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
        Attribute other = (Attribute) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString() + " " + getName();
    }
}
