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

import java.util.Iterator;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ComplexType;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 * @author Lukas Fryc
 */
public class ForEachStatement extends FreeMarkerTemplateStatementBase {

    private static final String FOR_EACH_LOOP_CLASS = "org.richfaces.ui.common.ForEachLoop";
    private static final String FOR_EACH_LOOP_STATUS_CLASS = FOR_EACH_LOOP_CLASS + ".Status";
    private static String LOOP_OBJECT_PREFIX = "forEach";
    private static volatile int LOOP_OBJECT_COUNTER = 1;

    private String itemsExpression;
    private TypedTemplateStatement itemsStatement;
    private ELType itemsType;
    private String var;
    private String loopObject;
    private String varStatus;
    private Integer begin;
    private Integer end;
    private Integer step;
    private ELType varType;
    private final ELParser parser;
    private final Logger log;
    private final TypesFactory typesFactory;

    private boolean iterator = false;
    private boolean iterable = false;

    @Inject
    public ForEachStatement(@TemplateModel FreeMarkerRenderer renderer, ELParser parser, Logger log, TypesFactory typesFactory) {
        super(renderer, "for-each");
        this.parser = parser;
        this.log = log;
        this.typesFactory = typesFactory;
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

    public String getVarStatus() {
        return varStatus;
    }

    public Integer getBegin() {
        return begin;
    }

    public Integer getEnd() {
        return end;
    }

    public Integer getStep() {
        return step;
    }

    public String getLoopObject() {
        return loopObject;
    }

    public boolean isLoopObjectRequired() {
        return varStatus != null || begin != null || end != null || step != null;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param itemsExpression the itemsExpression to set
     * @param var
     */
    public void setItemsExpression(String itemsExpression, String var, String varStatus, Integer begin, Integer end,
            Integer step) {
        try {
            this.itemsExpression = itemsExpression;
            this.itemsStatement = parser.parse(itemsExpression, this, Object.class.getName());
            this.itemsType = this.itemsStatement.getType();
            this.itemsStatement.setParent(this);
            this.varType = this.itemsStatement.getType().getContainerType();
            this.var = var;
            this.varStatus = varStatus;
            this.begin = begin;
            this.end = end;
            this.step = step;
            setVariable(var, this.varType);

            if (isLoopObjectRequired()) {
                initializeLoopObjectIteration();
            } else {
                initializePrimitiveIteration();
            }

        } catch (ParsingException e) {
            log.error("Error parsing expression for iteration in <foreach> statement", e);
        }
    }

    public void initializePrimitiveIteration() {
        if (this.itemsType.isArray()) {
            iterable = true;
        } else {
            if (this.itemsType instanceof ComplexType) {
                this.itemsType = ((ComplexType) this.itemsType).getRawType();
            }

            if (typesFactory.getType(Iterable.class).isAssignableFrom(this.itemsType)) {
                iterable = true;
            } else if (typesFactory.getType(Iterator.class).isAssignableFrom(this.itemsType)) {
                iterator = true;
            }
        }

        if (!iterable && !iterator) {
            log.info("The <foreach> expression " + itemsExpression
                    + " is not array, Iterable nor Iterator. It will be treated as single object.");
        }
    }

    public void initializeLoopObjectIteration() {
        this.loopObject = LOOP_OBJECT_PREFIX + LOOP_OBJECT_COUNTER++;

        this.addImport(FOR_EACH_LOOP_CLASS);

        setVariable(loopObject, typesFactory.getType(FOR_EACH_LOOP_CLASS));
        if (varStatus != null) {
            setVariable(varStatus, typesFactory.getType(FOR_EACH_LOOP_STATUS_CLASS));
        }

    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return Iterables.concat(super.getRequiredImports(), itemsStatement.getRequiredImports());
    }

    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        return Iterables.concat(super.getRequiredMethods(), itemsStatement.getRequiredMethods());
    }

    @Override
    public Iterable<JavaField> getRequiredFields() {
        return Iterables.concat(super.getRequiredFields(), itemsStatement.getRequiredFields());
    }

    public boolean isIterable() {
        return iterable;
    }

    public boolean isIterator() {
        return iterator;
    }
}
