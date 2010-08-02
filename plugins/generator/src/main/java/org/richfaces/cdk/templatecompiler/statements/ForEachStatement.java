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

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 */
public class ForEachStatement extends FreeMarkerTemplateStatementBase {

    private TypedTemplateStatement itemsStatement;

    private String var;

    private ELType varType;

    private final ELParser parser;

    private final Logger log;

    @Inject
    public ForEachStatement(@TemplateModel FreeMarkerRenderer renderer,ELParser parser,Logger log) {
        super(renderer,"for-each");
        this.parser = parser;
        this.log = log;
    }

    /**
     * @return the itemsExpression
     */
    public TypedTemplateStatement getItemsExpression() {
        return itemsStatement;
    }

    /**
     * @return the var
     */
    public String getVar() {
        return var;
    }

    /**
     * @return the varType
     */
    public ELType getVarType() {
        return varType;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param itemsExpression the itemsExpression to set
     * @param var 
     */
    public void setItemsExpression(String itemsExpression, String var) {
        try {
            this.itemsStatement = parser.parse(itemsExpression, this, Iterable.class.getName());
            this.itemsStatement.setParent(this);
            this.varType = this.itemsStatement.getType().getContainerType();
            this.var=var;
            setVariable(var, this.varType);
        } catch (ParsingException e) {
            log.error("Error parsing expression for iteration in <foreach> statement",e);
        }
    }
    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return Iterables.concat(super.getRequiredImports(),itemsStatement.getRequiredImports());
    }
    
    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        return Iterables.concat(super.getRequiredMethods(),itemsStatement.getRequiredMethods());
    }
    
    @Override
    public Iterable<JavaField> getRequiredFields() {
        return Iterables.concat(super.getRequiredFields(),itemsStatement.getRequiredFields());
    }

}
