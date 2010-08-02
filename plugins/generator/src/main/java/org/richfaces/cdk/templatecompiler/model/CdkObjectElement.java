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
package org.richfaces.cdk.templatecompiler.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.CdkException;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
@XmlRootElement(name = "object", namespace = Template.CDK_NAMESPACE)
public class CdkObjectElement implements ModelElement {

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    private String name;

    @XmlAttribute
    private String value;

    @XmlValue
    private String bodyValue;

    @XmlAttribute(required = true)
    private String type;

    @XmlAttribute(name = "type-arguments")
    private String typeArguments;

    /**
     * @return the bodyValue
     */
    public String getBodyValue() {
        return bodyValue;
    }

    /**
     * @param bodyValue the bodyValue to set
     */
    public void setBodyValue(String bodyValue) {
        this.bodyValue = bodyValue;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the typeArguments
     */
    public String getTypeArguments() {
        return typeArguments;
    }
    
    /**
     * @param typeArguments the typeArguments to set
     */
    public void setTypeArguments(String typeArguments) {
        this.typeArguments = typeArguments;
    }
    
    @Override
    public void visit(TemplateVisitor visitor) throws CdkException {
        visitor.visitElement(this);
    }

}
