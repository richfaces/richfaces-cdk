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

package org.richfaces.cdk.model;

import javax.xml.bind.annotation.XmlType;

import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.util.ComparatorUtils;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@XmlType(name = "function-configType", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
public class FunctionModel extends DescriptionGroupBase implements Named, ModelElement<FunctionModel> {

    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private static final long serialVersionUID = -358069932548425030L;

    private String name;

    private String signature;

    private TagType type;

    private ClassName functionClass;

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the name
     */
    @Merge
    public String getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the signature
     */
    @Merge
    public String getSignature() {
        return this.signature;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param signature
     *            the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the type
     */
    @Merge
    public TagType getType() {
        return this.type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param type
     *            the type to set
     */
    public void setType(TagType type) {
        this.type = type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the functionClass
     */
    @Merge
    public ClassName getFunctionClass() {
        return this.functionClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param functionClass
     *            the functionClass to set
     */
    public void setFunctionClass(ClassName functionClass) {
        this.functionClass = functionClass;
    }

    @Override
    public void merge(FunctionModel other) {
        ComponentLibrary.merge(this, other);
    }

    @Override
    public boolean same(FunctionModel other) {
        return ComparatorUtils.nullSafeEquals(getName(), other.getName());
    }

    @Override
    public <R,D> R accept(Visitor<R,D> visitor, D data) {
        return visitor.visitFunction(this,data);
    }

}
