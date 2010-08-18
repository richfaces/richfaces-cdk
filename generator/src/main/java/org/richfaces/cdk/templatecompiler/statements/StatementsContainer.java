/**
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.builder.model.Variables;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class StatementsContainer implements TemplateStatement, Variables {

    private static final Function<TemplateStatement, Iterable<JavaImport>> IMPORTS_TRANSFORM =
        new Function<TemplateStatement, Iterable<JavaImport>>() {

            @Override
            public Iterable<JavaImport> apply(TemplateStatement from) {
                return from.getRequiredImports();
            }
        };

    private static final Function<TemplateStatement, Iterable<JavaField>> FIELDS_TRANSFORM =
        new Function<TemplateStatement, Iterable<JavaField>>() {

            @Override
            public Iterable<JavaField> apply(TemplateStatement from) {
                return from.getRequiredFields();
            }
        };

    private static final Function<TemplateStatement, Iterable<HelperMethod>> HELPER_METHODS_TRANSFORM =
        new Function<TemplateStatement, Iterable<HelperMethod>>() {

            @Override
            public Iterable<HelperMethod> apply(TemplateStatement from) {
                return from.getRequiredMethods();
            }
        };

    @SuppressWarnings("serial")
    private final List<TemplateStatement> statements = new ArrayList<TemplateStatement>() {

        public boolean add(TemplateStatement e) {
            e.setParent(StatementsContainer.this);
            return super.add(e);
        };

        public void add(int index, TemplateStatement element) {
            element.setParent(StatementsContainer.this);
            super.add(index, element);
        };
    };

    private StatementsContainer parent;

    private final Map<String, ELType> localVariablesMap = Maps.newHashMap();

    public List<TemplateStatement> getStatements() {
        return statements;
    }

    public void addStatement(TemplateStatement statement) {
        getStatements().add(statement);
    }

    public void addStatement(int index, TemplateStatement statement) {
        getStatements().add(index, statement);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param parent
     *            the parent to set
     */
    public void setParent(StatementsContainer parent) {
        this.parent = parent;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the parent
     */
    public StatementsContainer getParent() {
        return parent;
    }

    public boolean isEmpty() {
        return getStatements().isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.templatecompiler.builder.model.JavaStatement#getCode()
     */
    @Override
    public String getCode() {
        StringBuilder sb = new StringBuilder();
        for (TemplateStatement statement : getStatements()) {
            sb.append(statement.getCode());
            sb.append('\n');
        }

        return sb.toString();
    }

    @Override
    public ELType getVariable(String name) {
        ELType type = localVariablesMap.get(name);
        if (null == type && null != parent) {
            type = parent.getVariable(name);
        }
        return type;
    }

    @Override
    public boolean isDefined(String name) {
        boolean defined = localVariablesMap.containsKey(name);
        if (!defined && null != parent) {
            defined = parent.isDefined(name);
        }
        return defined;
    }

    @Override
    public ELType setVariable(String name, ELType type) {
        ELType variable = getVariable(name);
        localVariablesMap.put(name, type);
        return variable;
    }

    private <T> Iterable<T> concatStatements(Function<TemplateStatement, Iterable<T>> transform) {
        Iterable<T> imports = Iterables.concat(Iterables.transform(getStatements(), transform));
        return imports;
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return concatStatements(IMPORTS_TRANSFORM);
    }

    @Override
    public Iterable<JavaField> getRequiredFields() {
        return concatStatements(FIELDS_TRANSFORM);
    }

    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        return concatStatements(HELPER_METHODS_TRANSFORM);
    }

//    @Override
//    public String toString() {
//        return getCode();
//    }
    
}
