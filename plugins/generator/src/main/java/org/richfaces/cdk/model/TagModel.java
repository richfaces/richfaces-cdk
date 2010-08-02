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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.util.ComparatorUtils;
import org.richfaces.cdk.xmlconfig.model.ClassAdapter;

/**
 * <p class="changed_added_4_0">That bean represents VDL tag</p>
 * @author asmirnov@exadel.com
 *
 */
@XmlType(name = "tag-configType", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class TagModel implements Named , ModelElement<TagModel> {

    private static final long serialVersionUID = 3875718626199223087L;

    private ClassName targetClass;
    
    private ClassName baseClass;
    
    private TagType type;
    
    private boolean generate = false;
    /**
     *  <p class="changed_added_4_0">Tag name</p>
     */
    private  String name;

    @XmlElement(name = "tag-name", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public String getName() {
        return name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * <p class="changed_added_4_0"></p>
     * @return the type
     */
    @XmlElement(name = "tag-type", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public TagType getType() {
        return this.type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param type the type to set
     */
    public void setType(TagType type) {
        this.type = type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the targetClass
     */
    @Merge
    @XmlElement(name = "handler-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getTargetClass() {
        return this.targetClass;
    }


    /**
     * <p class="changed_added_4_0"></p>
     * @param targetClass the targetClass to set
     */
    public void setTargetClass(ClassName targetClass) {
        this.targetClass = targetClass;
    }


    /**
     * <p class="changed_added_4_0"></p>
     * @return the baseClass
     */
    @Merge
    @XmlElement(name = "base-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getBaseClass() {
        return this.baseClass;
    }


    /**
     * <p class="changed_added_4_0"></p>
     * @param baseClass the baseClass to set
     */
    public void setBaseClass(ClassName baseClass) {
        this.baseClass = baseClass;
    }


    /**
     * <p class="changed_added_4_0"></p>
     * @return the generate
     */
    public boolean isGenerate() {
        return this.generate;
    }


    /**
     * <p class="changed_added_4_0"></p>
     * @param generate the generate to set
     */
    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    @Override
    public void merge(TagModel other) {
        ComponentLibrary.merge(this, other);        
    }

    @Override
    public boolean same(TagModel other) {
        return ComparatorUtils.nullSafeEquals(this.getName(), other.getName()) && ComparatorUtils.nullSafeEquals(this.getType(), other.getType());
    }

    @Override
    public <R,D> R accept(Visitor<R,D> visitor, D data) {
        return visitor.visit(this,data);        
    }
    
}
