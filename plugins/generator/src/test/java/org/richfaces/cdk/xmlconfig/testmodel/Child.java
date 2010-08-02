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

package org.richfaces.cdk.xmlconfig.testmodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@XmlType(name = "ChildType", namespace = Root.HTTP_FOO_BAR_SCHEMA)
public class Child implements Id {
    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @author asmirnov@exadel.com
     * 
     */
    public static class Extension {
        private List<Element> extensions = new ArrayList<Element>();
        private Child parent;

        // This method is called after all the properties (except IDREF) are unmarshalled for this object,
        // but before this object is set to the parent object.
        void afterUnmarshal(Unmarshaller u, Object parent) {
        }

        // This method is called immediately after the object is created and before the unmarshalling of this
        // object begins. The callback provides an opportunity to initialize JavaBean properties prior to unmarshalling.
        void beforeUnmarshal(Unmarshaller u, Object parent) {
            this.parent = (Child) parent;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @return the extensions
         */
        @XmlAnyElement
        public List<Element> getExtensions() {
            return extensions;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @return the myExtension
         */
        @XmlElement(namespace = Root.EXTENSIONS_NAMESPACE)
        public String getMyExtension() {
            return parent.getWrapped();
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @param extensions
         *            the extensions to set
         */
        public void setExtensions(List<Element> extensions) {
            this.extensions = extensions;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @param myExtension
         *            the myExtension to set
         */
        public void setMyExtension(String myExtension) {
            parent.setWrapped(myExtension);
        }
    }
    private Extension extension;
    private String id;
    private String value;

    private String wrapped;

    /*
     * (non-Javadoc)
     * 
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

        if (!(obj instanceof Child)) {
            return false;
        }

        Child other = (Child) obj;

        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }

        return true;
    }

    @XmlElement(namespace = Root.HTTP_FOO_BAR_SCHEMA)
    public Extension getExtension() {
        if (extension == null) {
            extension = new Extension();
        }

        return extension;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.xmlconfig.testmodel.Id#getId()
     */
    @Override
    @XmlElement(namespace = Root.HTTP_FOO_BAR_SCHEMA)
    public String getId() {
        return id;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the value
     */
    @XmlElement(namespace = Root.HTTP_FOO_BAR_SCHEMA)
    public String getValue() {
        return value;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the wrapped
     */
    public String getWrapped() {
        return wrapped;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((id == null) ? 0 : id.hashCode());

        return result;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param wrapped
     *            the wrapped to set
     */
    public void setWrapped(String wrapped) {
        this.wrapped = wrapped;
    }
}
