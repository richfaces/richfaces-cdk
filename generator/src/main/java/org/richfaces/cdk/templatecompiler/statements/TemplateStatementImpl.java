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
package org.richfaces.cdk.templatecompiler.statements;

import java.util.Collections;

import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class TemplateStatementImpl implements TemplateStatement {
    private final String code;
    private StatementsContainer parent;

    public TemplateStatementImpl(String code) {
        this.code = code;
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.builder.model.JavaStatement#getCode()
     */
    @Override
    public String getCode() {
        return code;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.builder.model.RequireImports#getRequiredImports()
     */
    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return Collections.emptySet();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.statements.TemplateStatement#getRequiredFields()
     */
    @Override
    public Iterable<JavaField> getRequiredFields() {
        return Collections.emptySet();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.statements.TemplateStatement#getRequiredMethods()
     */
    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        return Collections.emptySet();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.richfaces.cdk.templatecompiler.statements.TemplateStatement#setParent(org.richfaces.cdk.templatecompiler.statements
     * .StatementsContainer)
     */
    @Override
    public void setParent(StatementsContainer parent) {
        this.parent = parent;
    }
}
