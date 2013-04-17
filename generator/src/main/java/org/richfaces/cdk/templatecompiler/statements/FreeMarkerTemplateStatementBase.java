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

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImportImpl;
import org.richfaces.cdk.templatecompiler.builder.model.JavaModifier;
import org.richfaces.cdk.templatecompiler.el.types.ReferencedType;
import org.richfaces.cdk.util.Strings;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 *
 */
public class FreeMarkerTemplateStatementBase extends StatementsContainer {
    protected StatementsContainer parent;
    private String templateName;
    private final FreeMarkerRenderer renderer;
    private final Set<JavaImport> imports = Sets.newTreeSet(JavaImport.COMPARATOR);
    private final EnumSet<HelperMethod> requiredMethods = EnumSet.noneOf(HelperMethod.class);
    private final List<JavaField> fields = Lists.newArrayList();
    private boolean parsed = false;
    private String code;

    protected FreeMarkerTemplateStatementBase(FreeMarkerRenderer renderer, String templateName) {
        super();
        this.renderer = renderer;
        this.templateName = templateName + ".ftl";
    }

    @Override
    public String getCode() {
        parse();
        return code;
    }

    private void parse() {
        if (!parsed) {
            code = renderer.renderTemplate(templateName, this);
            parsed = true;
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Some templates use modelItem variable
     * </p>
     *
     * @return
     */
    public Object getModelItem() {
        return this;
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        parse();
        return Iterables.concat(super.getRequiredImports(), imports);
    }

    @Override
    public Iterable<JavaField> getRequiredFields() {
        parse();
        return Iterables.concat(super.getRequiredFields(), fields);
    }

    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        parse();
        return Iterables.concat(super.getRequiredMethods(), requiredMethods);
    }

    protected void addRequiredMethods(HelperMethod... methods) {
        for (HelperMethod helperMethod : methods) {
            requiredMethods.add(helperMethod);
        }
    }

    protected void addRequiredMethods(Iterable<HelperMethod> methods) {
        for (HelperMethod helperMethod : methods) {
            requiredMethods.add(helperMethod);
        }
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName + ".ftl";
    }

    public void addConstant(String type, String name, String code) {
        JavaField field = new JavaField(new ReferencedType(type), name);
        field.addModifier(JavaModifier.PRIVATE);
        field.addModifier(JavaModifier.STATIC);
        field.addModifier(JavaModifier.FINAL);
        if (!Strings.isEmpty(code)) {
            field.setValue(new TemplateStatementImpl(code));
        }
        fields.add(field);
    }

    public void addImport(String name) {
        imports.add(new JavaImportImpl(name));
    }

    protected void addImports(Iterable<JavaImport> requiredImports) {
        Iterables.addAll(imports, requiredImports);
    }

    public void addRequiredMethod(String helperMethodName) {
        HelperMethod helperMethod = HelperMethod.valueOf(helperMethodName);
        requiredMethods.add(helperMethod);
    }
}