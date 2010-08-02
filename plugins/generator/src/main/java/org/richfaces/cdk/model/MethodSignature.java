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

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0"></p>
 * @author asmirnov@exadel.com
 *
 */
@SuppressWarnings("serial")
@XmlType(name = "signature", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class MethodSignature implements Serializable {
    
    private ClassName returnType = ClassName.get(Void.class);
    
    private List<ClassName> parameters = Lists.newArrayList();

    /**
     * <p class="changed_added_4_0"></p>
     * @return the returnType
     */
    @XmlElement(name = "return-type", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public ClassName getReturnType() {
        return this.returnType;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param returnType the returnType to set
     */
    public void setReturnType(ClassName returnType) {
        this.returnType = returnType;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the parameters
     */
    @XmlElement(name = "param", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public List<ClassName> getParameters() {
        return this.parameters;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param parameters the parameters to set
     */
    public void setParameters(List<ClassName> parameters) {
        this.parameters = parameters;
    }


}
