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
package org.richfaces.cdk.xmlconfig.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConfigExtension;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.MethodSignature;

import com.google.common.collect.Sets;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class PropertyBase extends ExtensibleBean<PropertyBase.PropertyExtension> {
    private String defaultValue;
    private String name;
    private String suggestedValue;
    private ClassName type;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the name
     */
    public String getName() {
        return name;
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
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the suggestedValue
     */
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

    @Override
    public PropertyExtension getExtension() {
        return super.getExtension();
    }

    @Override
    public void setExtension(PropertyExtension extension) {
        super.setExtension(extension);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @author asmirnov@exadel.com
     */
    public static class PropertyExtension extends ConfigExtension {
        private Boolean passThrough;
        private Boolean defaultBehavior;
        private Set<EventName> eventNames = Sets.newLinkedHashSet();
        private MethodSignature signature;
        private String aliasFor;
        private Boolean generate;
        private Boolean hidden;
        private Boolean literal;
        private Boolean readOnly;
        private Boolean required;

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the generate
         */
        @XmlElement(name = "generate", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getGenerate() {
            return generate;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param generate the generate to set
         */
        public void setGenerate(Boolean generate) {
            this.generate = generate;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the hidden
         */
        @XmlElement(name = "hidden", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getHidden() {
            return hidden;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param hidden the hidden to set
         */
        public void setHidden(Boolean hidden) {
            this.hidden = hidden;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the literal
         */
        @XmlElement(name = "literal", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getLiteral() {
            return literal;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param literal the literal to set
         */
        public void setLiteral(Boolean literal) {
            this.literal = literal;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the required
         */
        @XmlElement(name = "required", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getRequired() {
            return required;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param required the required to set
         */
        public void setRequired(Boolean required) {
            this.required = required;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the readOnly
         */
        @XmlElement(name = "read-only", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getReadOnly() {
            return readOnly;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param readOnly the readOnly to set
         */
        public void setReadOnly(Boolean readOnly) {
            this.readOnly = readOnly;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the passThrough
         */
        @XmlElement(name = "pass-through", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getPassThrough() {
            return passThrough;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param passThrough the passThrough to set
         */
        public void setPassThrough(Boolean passThrough) {
            this.passThrough = passThrough;
        }

        /**
         * @return the defaultBehavior
         */
        @XmlElement(name = "default-behavior", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
        public Boolean getDefaultBehavior() {
            return defaultBehavior;
        }

        /**
         * @param defaultBehavior the passThrough to set
         */
        public void setDefaultBehavior(Boolean defaultBehavior) {
            this.defaultBehavior = defaultBehavior;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the eventNames
         */
        @XmlElement(name = "event-name", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
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
         * @return the signature
         */
        @XmlElement(name = "signature", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
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

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the aliasFor
         */
        @XmlElement(name = "aliasFor", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public String getAliasFor() {
            return aliasFor;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param aliasFor the aliasFor to set
         */
        public void setAliasFor(String aliases) {
            this.aliasFor = aliases;
        }
    }
}
