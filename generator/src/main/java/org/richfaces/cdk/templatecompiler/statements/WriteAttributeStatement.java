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
package org.richfaces.cdk.templatecompiler.statements;

import java.util.Collections;

import javax.xml.namespace.QName;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 */
public class WriteAttributeStatement extends FreeMarkerTemplateStatementBase {
    private QName attributeName;
    private TypedTemplateStatement valueExpression;
    private final ELParser parser;
    private final Logger log;
    private String defaultValue;
    private Iterable<String> events = Collections.emptyList();

    @Inject
    public WriteAttributeStatement(@TemplateModel FreeMarkerRenderer renderer, ELParser parser, Logger log) {
        super(renderer, "write-attribute");
        this.parser = parser;
        this.log = log;
    }

    public void setAttribute(QName qName, Object object, String defaultValue) {
        this.defaultValue = defaultValue;
        setAttributeName(qName);
        parseExpression(object, TypesFactory.OBJECT_TYPE);
    }

    public void setUriAttribute(QName qName, Object value, String defaultValue) {
        setTemplateName("write-uri-attribute");
        setAttribute(qName, value, defaultValue);
    }

    public void setBooleanAttribute(QName qName, Object value, String defaultValue) {
        setTemplateName("write-boolean-attribute");
        setAttribute(qName, value, defaultValue);
    }

    private void parseExpression(Object object, ELType objectType) {
        try {
            valueExpression = parser.parse(object.toString(), this, objectType);
            this.addStatement(valueExpression);
        } catch (ParsingException e) {
            log.error("Error parsing expression for attribute " + getAttributeName(), e);
        }
    }

    private void setAttributeName(QName qName) {
        this.attributeName = qName;
    }

    public QName getAttributeName() {
        return attributeName;
    }

    public TypedTemplateStatement getValue() {
        return valueExpression;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the defaultValue
     */
    public String getDefaultValue() {
        // TODO - convert to Java syntax according to type.
        return this.defaultValue;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param iterable the events to set
     */
    public void setEvents(Iterable<String> iterable) {
        this.events = iterable;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the events
     */
    public Iterable<String> getEvents() {
        return this.events;
    }
}
