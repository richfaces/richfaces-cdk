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
package org.richfaces.builder.mojo;

/**
 * @author shura
 *
 */
public class Taglib {
    private String excludeModules = null;
    private String excludeTags = null;
    private String includeModules = null;
    private String includeTags = null;
    private String tlibVersion = null;
    private String validatorClass = null;
    private String displayName;
    private String jspVersion;
    private String listenerClass;
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
    public String getListenerClass() {
        return this.listenerClass;
    }

    /**
     * @param listenerClass the listenerClass to set
     */
    public void setListenerClass(String listenerClass) {
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
    public String getValidatorClass() {
        return this.validatorClass;
    }

    /**
     * @param validatorClass the validatorClass to set
     */
    public void setValidatorClass(String validatorClass) {
        this.validatorClass = validatorClass;
    }

    /**
     * @return the includeModules
     */
    public String getIncludeModules() {
        return includeModules;
    }

    /**
     * @param includeModules the includeModules to set
     */
    public void setIncludeModules(String includeModules) {
        this.includeModules = includeModules;
    }

    /**
     * @return the excludeModules
     */
    public String getExcludeModules() {
        return excludeModules;
    }

    /**
     * @param excludeModules the excludeModules to set
     */
    public void setExcludeModules(String excludeModules) {
        this.excludeModules = excludeModules;
    }

    /**
     * @return the includeTags
     */
    public String getIncludeTags() {
        return includeTags;
    }

    /**
     * @param includeTags the includeTags to set
     */
    public void setIncludeTags(String includeTags) {
        this.includeTags = includeTags;
    }

    /**
     * @return the excludeTags
     */
    public String getExcludeTags() {
        return excludeTags;
    }

    /**
     * @param excludeTags the excludeTags to set
     */
    public void setExcludeTags(String excludeTags) {
        this.excludeTags = excludeTags;
    }

    @Override
    public String toString() {
        return "Lib: " + getShortName() + ", URL: " + getUri();
    }
}
