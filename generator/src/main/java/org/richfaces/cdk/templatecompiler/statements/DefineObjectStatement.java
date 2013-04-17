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
import java.util.List;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.util.Strings;

import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 */
public class DefineObjectStatement extends FreeMarkerTemplateStatementBase {
    private ELType type;
    private String name;
    private boolean cast;
    private final ELParser parser;
    private final Logger log;
    private final TypesFactory typesFactory;
    private TypedTemplateStatement initializationStatement;

    @Inject
    public DefineObjectStatement(Logger log, @TemplateModel FreeMarkerRenderer renderer, ELParser parser,
            TypesFactory typesFactory) {
        super(renderer, "define-object");
        this.log = log;
        this.parser = parser;
        this.typesFactory = typesFactory;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param initializationExpression the initializationExpression to set
     * @throws ParsingException
     */
    public void setObject(String name, String type, String initializationExpression, boolean cast) {
        try {
            this.name = name;
            this.cast = cast;
            if (!Strings.isEmpty(initializationExpression)) {
                initializationStatement = parser.parse(initializationExpression, this, TypesFactory.OBJECT_TYPE);
                initializationStatement.setParent(this);
                this.type = initializationStatement.getType();
            } else {
                this.type = TypesFactory.OBJECT_TYPE;
            }
            if (!Strings.isEmpty(type)) {
                this.type = typesFactory.getType(type);
                addImports(this.type.getRequiredImports());
            }
        } catch (ParsingException e) {
            log.error("Error parse initialization expression for variable " + name, e);
        }
    }

    /**
     * @return the type
     */
    public ELType getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the initializationStatement
     */
    public TypedTemplateStatement getInitializationStatement() {
        return this.initializationStatement;
    }

    @Override
    public List<TemplateStatement> getStatements() {
        return null != initializationStatement ? Collections.<TemplateStatement>singletonList(initializationStatement)
                : Collections.<TemplateStatement>emptyList();
    }

    public boolean isCast() {
        return cast;
    }
}
