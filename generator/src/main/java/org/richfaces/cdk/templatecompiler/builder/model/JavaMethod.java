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
package org.richfaces.cdk.templatecompiler.builder.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author Maksim Kaszynski
 */
public class JavaMethod extends JavaLanguageElement {
    private List<Argument> arguments = Lists.newArrayList();
    private List<ELType> exceptions = Lists.newArrayList();
    private JavaStatement methodBody;
    private ELType returnType;

    public JavaMethod(String name) {
        super(name);
        this.returnType = TypesFactory.VOID_TYPE;
    }

    public JavaMethod(String name, Collection<Argument> arguments) {
        this(name);
        this.arguments.addAll(arguments);
    }

    public JavaMethod(String name, Argument... arguments) {
        this(name, Arrays.asList(arguments));
    }

    public JavaMethod(String name, ELType returnType, Argument... arguments) {
        this(name);
        this.returnType = returnType;
        this.arguments.addAll(Arrays.asList(arguments));
    }

    public List<ELType> getExceptions() {
        return exceptions;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public JavaStatement getMethodBody() {
        return methodBody;
    }

    public ELType getReturnType() {
        return returnType;
    }

    public void setMethodBody(JavaStatement methodBody) {
        this.methodBody = methodBody;
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        Iterable<JavaImport> exceptionsImports = Iterables.concat(Iterables.transform(getExceptions(),
                RequireImports.IMPORTS_TRANSFORM));
        Iterable<JavaImport> argumentsImports = Iterables.concat(Iterables.transform(getArguments(),
                RequireImports.IMPORTS_TRANSFORM));
        Iterable<JavaImport> imports = Iterables.concat(getReturnType().getRequiredImports(), exceptionsImports,
                argumentsImports);
        if (null != getMethodBody()) {
            imports = Iterables.concat(imports, getMethodBody().getRequiredImports());
        }
        return imports;
    }
}
