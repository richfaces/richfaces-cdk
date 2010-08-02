/**
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



package org.richfaces.cdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.xmlconfig.model.ClassAdapter;

/**
 * This class contains information about tag library.
 * @author asmirnov
 *
 */
@XmlType(name = "taglib-configType", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class Taglib {
    private String tlibVersion = null;
    private ClassName validatorClass = null;
    private String displayName;
    private String jspVersion;
    private ClassName listenerClass;
    private String shortName;
    private String taglib;
    private String uri;

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the listenerClass
     */
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getListenerClass() {
        return this.listenerClass;
    }

    /**
     * @param listenerClass the listenerClass to set
     */
    public void setListenerClass(ClassName listenerClass) {
        this.listenerClass = listenerClass;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @return the taglib
     */
    public String getTaglib() {
        return this.taglib;
    }

    /**
     * @param taglib the taglib to set
     */
    public void setTaglib(String taglib) {
        this.taglib = taglib;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return this.uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the jspVersion
     */
    public String getJspVersion() {
        return this.jspVersion;
    }

    /**
     * @param jspVersion the jspVersion to set
     */
    public void setJspVersion(String jspVersion) {
        this.jspVersion = jspVersion;
    }

    /**
     * @return the tlibVersion
     */
    public String getTlibVersion() {
        return this.tlibVersion;
    }

    /**
     * @param tlibVersion the tlibVersion to set
     */
    public void setTlibVersion(String tlibVersion) {
        this.tlibVersion = tlibVersion;
    }

    /**
     * @return the validatorClass
     */
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getValidatorClass() {
        return this.validatorClass;
    }

    /**
     * @param validatorClass the validatorClass to set
     */
    public void setValidatorClass(ClassName validatorClass) {
        this.validatorClass = validatorClass;
    }

    @Override
    public String toString() {
        return "Lib: " + getShortName() + ", URL: " + getUri();
    }
}
