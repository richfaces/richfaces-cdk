/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.model;

import java.util.Set;

import org.richfaces.cdk.util.ComparatorUtils;

import com.google.common.collect.Sets;

/**
 * @author akolonitsky
 * @since Mar 19, 2010
 */
public abstract class PropertyBase extends DescriptionGroupBase implements ModelElement<PropertyBase>, Named {
    private static final long serialVersionUID = 3483864006602184580L;
    /**
     * <p class="changed_added_4_0">
     * The name of bean property
     * </p>
     */
    private String name;
    private boolean hidden = false;
    private boolean literal = false;
    private boolean required = false;
    private boolean readOnly = false;
    private boolean passThrough = false;
    private Set<EventName> eventNames = Sets.newLinkedHashSet();
    private MethodSignature signature;
    private String defaultValue;
    private String suggestedValue;
    private ClassName type;
    private boolean bindingAttribute;
    private boolean binding;
    private String aliasFor;
    /**
     * <p class="changed_added_4_0">
     * Is that bean property generate in the class or should be generated ?
     * </p>
     */
    private Boolean generate;

    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    public PropertyBase() {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the type
     */
    @Merge
    public ClassName getType() {
        return type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param type the type to set
     */
    public void setType(ClassName type) {
        this.type = type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the aliasFor
     */
    @Merge
    public String getAliasFor() {
        return this.aliasFor;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param aliasFor the aliasFor to set
     */
    public void setAliasFor(String aliasFor) {
        this.aliasFor = aliasFor;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the defaultValue
     */
    @Merge
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param dafaultValue - the defaultValue to set
     */
    public void setDefaultValue(String dafaultValue) {
        this.defaultValue = dafaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the suggestedValue
     */
    @Merge
    public String getSuggestedValue() {
        return suggestedValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param suggestedValue the suggestedValue to set
     */
    public void setSuggestedValue(String suggestedValue) {
        this.suggestedValue = suggestedValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the eventNames
     */
    @Merge
    public Set<EventName> getEventNames() {
        return eventNames;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param eventNames the eventNames to set
     */
    public void setEventNames(Set<EventName> eventNames) {
        this.eventNames = eventNames;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the hidden
     */
    @Merge(preferTrue = true)
    public boolean isHidden() {
        return hidden;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the literal
     */
    public boolean isLiteral() {
        return literal;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param literal the literal to set
     */
    public void setLiteral(boolean literal) {
        this.literal = literal;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the passThrough
     */
    public boolean isPassThrough() {
        return passThrough;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param passThrough the passThrough to set
     */
    public void setPassThrough(boolean passThrough) {
        this.passThrough = passThrough;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the signature
     */
    @Merge(overwrite = true)
    public MethodSignature getSignature() {
        return signature;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param signature the signature to set
     */
    public void setSignature(MethodSignature signature) {
        this.signature = signature;
    }

    public boolean isPrimitive() {
        return getType().isPrimitive();
    }

    @Override
    public void merge(PropertyBase other) {
        ComponentLibrary.merge(this, other);
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        return visitor.visitProperty(this, data);
    }

    @Override
    public boolean same(PropertyBase other) {
        return ComparatorUtils.nullSafeEquals(this.getName(), other.getName());
    }

    public boolean isBindingAttribute() {
        return this.bindingAttribute;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param bindingAttribute the bindingAttribute to set
     */
    public void setBindingAttribute(boolean bindingAttribute) {
        this.bindingAttribute = bindingAttribute;
    }

    public boolean isBinding() {
        return this.binding;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param binding the binding to set
     */
    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    @Override
    public String toString() {
        return "PropertyModel {name: " + getName() + ", type: " + getType().getName() + "}";
    }

    @Merge
    public Boolean getGenerate() {
        return generate;
    }

    public void setGenerate(Boolean generate) {
        this.generate = generate;
    }
}
