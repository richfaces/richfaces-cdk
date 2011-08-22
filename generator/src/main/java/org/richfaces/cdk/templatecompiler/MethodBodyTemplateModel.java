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
package org.richfaces.cdk.templatecompiler;

import org.richfaces.cdk.templatecompiler.builder.model.JavaStatement;

import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class MethodBodyTemplateModel extends StringModel implements TemplateModel {
    private static final String CODE_ATTRIBUTE_NAME = "code";
    private final JavaStatement statement;
    private final JavaClassModelWrapper modelWrapper;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param statement
     * @param javaClassModelWrapper
     */
    public MethodBodyTemplateModel(JavaStatement statement, JavaClassModelWrapper javaClassModelWrapper) {
        super(statement, javaClassModelWrapper);
        this.statement = statement;
        this.modelWrapper = javaClassModelWrapper;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        if (CODE_ATTRIBUTE_NAME.equals(key)) {
            String statementCode = statement.getCode();
            return modelWrapper.wrap(statementCode);
        }
        return super.get(key);
    }

    @Override
    public String getAsString() {
        return statement.getCode();
    }
}
