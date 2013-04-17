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
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 */
public class WriteTextStatement extends FreeMarkerTemplateStatementBase {
    private TemplateStatement textStatement;
    private final ELParser parser;
    private final Logger log;

    @Inject
    public WriteTextStatement(@TemplateModel FreeMarkerRenderer renderer, ELParser parser, Logger log) {
        super(renderer, "write-text");
        this.parser = parser;
        this.log = log;
    }

    public TemplateStatement getValue() {
        return textStatement;
    }

    @Override
    public List<TemplateStatement> getStatements() {
        return Collections.singletonList(textStatement);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param textExpression the textExpression to set
     * @throws ParsingException
     */
    public void setExpression(String textExpression) {
        try {
            textStatement = parser.parse(textExpression, this, TypesFactory.OBJECT_TYPE);
            textStatement.setParent(this);
        } catch (ParsingException e) {
            log.error("Error parsing EL expression", e);
        }
    }
}
