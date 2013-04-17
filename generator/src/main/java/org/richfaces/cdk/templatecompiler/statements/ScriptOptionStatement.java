/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import static org.richfaces.cdk.templatecompiler.builder.model.RequireImports.IMPORTS_TRANSFORM;
import static org.richfaces.cdk.templatecompiler.statements.TemplateStatement.FIELDS_TRANSFORM;

import java.util.Collections;
import java.util.List;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 *
 */
public class ScriptOptionStatement extends FreeMarkerTemplateStatementBase {
    private String name;
    private String defaultValue;
    private TypedTemplateStatement value;
    private String wrapper;
    private ELParser parser;
    private Logger logger;

    @Inject
    public ScriptOptionStatement(@TemplateModel FreeMarkerRenderer renderer, ELParser parser, Logger logger) {
        super(renderer, "script-option");

        this.parser = parser;
        this.logger = logger;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param value the value to set
     */
    public void setValueExpression(String valueExpression) {
        try {
            this.value = parser.parse(valueExpression, this, TypesFactory.OBJECT_TYPE);
        } catch (ParsingException e) {
            logger.error("Error parse scriptOption value expression: " + valueExpression, e);
        }
    }

    /**
     * @param wrapper the wrapper to set
     */
    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public List<TemplateStatement> getStatements() {
        return value != null ? Collections.<TemplateStatement>singletonList(value) : Collections.<TemplateStatement>emptyList();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public TypedTemplateStatement getValue() {
        return value;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return the wrapper
     */
    public String getWrapper() {
        return wrapper;
    }

    @Override
    public Iterable<JavaField> getRequiredFields() {
        return Iterables.concat(super.getRequiredFields(), FIELDS_TRANSFORM.apply(value));
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return Iterables.concat(super.getRequiredImports(), IMPORTS_TRANSFORM.apply(value));
    }

    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        return Iterables.concat(super.getRequiredMethods(), METHODS_TRANSFORM.apply(value));
    }
}
