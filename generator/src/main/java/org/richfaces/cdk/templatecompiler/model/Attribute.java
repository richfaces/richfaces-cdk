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

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.xmlconfig.model.ClassAdapter;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
public class Attribute implements Serializable {

    private static final long serialVersionUID = -8183353368681247171L;
    
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute
    private String displayName;

    @XmlAttribute
    private String shortDescription;

    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlAttribute(name = "default")
    private String defaultValue;

    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlAttribute(name = "method-signature")
    private String methodSignature;

    @XmlAttribute
    private String targets;

    @XmlAttribute
    private boolean required;

    @XmlAttribute
    private boolean preferred;

    @XmlAttribute
    private boolean expert;

    @XmlAttribute
    @XmlJavaTypeAdapter(ClassAdapter.class)
    private ClassName type = new ClassName(Object.class);

    @XmlElement(name = "clientBehavior", namespace = Template.COMPOSITE_NAMESPACE)
    private List<ClientBehavior> clientBehaviors=Lists.newArrayList();
    
    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the displayName
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the shortDescription
     */
    public String getShortDescription() {
        return this.shortDescription;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the methodSignature
     */
    public String getMethodSignature() {
        return this.methodSignature;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param methodSignature the methodSignature to set
     */
    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the targets
     */
    public String getTargets() {
        return this.targets;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param applyTo the targets to set
     */
    public void setTargets(String applyTo) {
        this.targets = applyTo;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the required
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the preffered
     */
    public boolean isPreferred() {
        return this.preferred;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param preffered the preffered to set
     */
    public void setPreferred(boolean preffered) {
        this.preferred = preffered;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the expert
     */
    public boolean isExpert() {
        return this.expert;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param expert the expert to set
     */
    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the type
     */
    public ClassName getType() {
        return type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param type the type to set
     */
    public void setType(ClassName type) {
        this.type = type;
    }
    
    /**
     * <p class="changed_added_4_0"></p>
     * 
     * @return the clientBehaviors
     */
    public List<ClientBehavior> getClientBehaviors() {
        return clientBehaviors;
    }
    
    /**
     * <p class="changed_added_4_0"></p>
     * 
     * @param clientBehaviors the clientBehaviors to set
     */
    public void setClientBehaviors(List<ClientBehavior> clientBehaviors) {
        this.clientBehaviors = clientBehaviors;
    }
}
