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
public class Renderkit {
    private String markup = "html";
    private String name = "HTML_BASIC";
    private String classPackage;
    private String classname;
    private StringBuffer content;

    /**
     * @return the classname
     */
    public String getClassname() {
        return this.classname;
    }

    /**
     * @param classname the classname to set
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * @return the markup
     */
    public String getMarkup() {
        return this.markup;
    }

    /**
     * @param markup the markup to set
     */
    public void setMarkup(String markup) {
        this.markup = markup;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the package
     */
    public String getPackage() {
        return this.classPackage;
    }

    /**
     * @param package1 the package to set
     */
    public void setPackage(String package1) {
        this.classPackage = package1;
    }

    /**
     * @return the content
     */
    StringBuffer getContent() {
        return this.content;
    }

    /**
     * @param content the content to set
     */
    void setContent(StringBuffer content) {
        this.content = content;
    }

    public String getFacesConfig() {
        if (null != content) {
            return content.toString();
        }

        return "";
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "render-kit id [" + name + "], class [" + classname + "], content :" + content;
    }
}
