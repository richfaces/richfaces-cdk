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

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 *
 */
// TODO - configure initial map size
public class ScriptObjectStatement extends FreeMarkerTemplateStatementBase {
    private String name;
    private ELParser parser;
    private TypedTemplateStatement base;
    private Logger logger;
    private TypesFactory typesFactory;
    private ELType mapType;
    private ELType mapImplementationType;

    @Inject
    public ScriptObjectStatement(@TemplateModel FreeMarkerRenderer renderer, ELParser parser, Logger logger,
            TypesFactory typesFactory) {

        super(renderer, "script-object");

        this.parser = parser;
        this.logger = logger;
        this.typesFactory = typesFactory;
    }

    /**
     * @return the keyExpression
     */
    public TemplateStatement getBaseExpression() {
        return base;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public ELType getType() {
        return mapType;
    }

    public ELType getImplementationType() {
        return mapImplementationType;
    }

    public void setObject(String name, String baseExpression) {
        this.mapType = typesFactory.getType("Map<String, Object>");
        this.mapImplementationType = typesFactory.getType("LinkedHashMap<String, Object>");
        this.name = name;

        if (!Strings.isNullOrEmpty(baseExpression)) {
            try {
                this.base = parser.parse(baseExpression, this, getType());
                base.setParent(this);
            } catch (ParsingException e) {
                logger.error("Error parse scriptObject statement expression", e);
            }
        }

        setVariable(name, getType());
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return Iterables.concat(super.getRequiredImports(), mapImplementationType.getRequiredImports(),
                mapType.getRequiredImports(), IMPORTS_TRANSFORM.apply(base));
    }

    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        return Iterables.concat(super.getRequiredMethods(), METHODS_TRANSFORM.apply(base));
    }

    @Override
    public Iterable<JavaField> getRequiredFields() {
        return Iterables.concat(super.getRequiredFields(), FIELDS_TRANSFORM.apply(base));
    }
}
