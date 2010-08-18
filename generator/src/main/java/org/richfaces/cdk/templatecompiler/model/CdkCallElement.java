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

package org.richfaces.cdk.templatecompiler.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.richfaces.cdk.CdkException;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlRootElement(name = "call", namespace = Template.CDK_NAMESPACE)
public class CdkCallElement implements ModelElement {

    private String expression;

    @XmlValue
    private String bodyValue;

    public CdkCallElement() {
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the expression
     */
    @XmlAttribute
    public String getExpression() {
        return this.expression;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param expression the expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the bodyValue
     */
    public String getBodyValue() {
        return bodyValue;
    }
    
    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param bodyValue the bodyValue to set
     */
    public void setBodyValue(String bodyValue) {
        this.bodyValue = bodyValue;
    }
    
    @Override
    public void visit(TemplateVisitor visitor) throws CdkException {

        visitor.visitElement(this);

    }

}
