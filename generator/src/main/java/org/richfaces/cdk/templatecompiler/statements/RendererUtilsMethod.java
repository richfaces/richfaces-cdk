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

import java.util.TreeSet;

import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImportImpl;
import org.richfaces.cdk.templatecompiler.builder.model.JavaMethod;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class RendererUtilsMethod extends JavaMethod {
    public static final String BUILT_IN = "#built-in";
    private TreeSet<JavaImport> runtimeImport = Sets.newTreeSet(JavaImport.COMPARATOR);

    public RendererUtilsMethod(HelperMethod helper, String rendererUtilsClassName) {
        super(helper.getName());
        this.runtimeImport.add(new JavaImportImpl(rendererUtilsClassName + ".*", true));
        this.runtimeImport.add(new JavaImportImpl(rendererUtilsClassName));
        this.runtimeImport.add(new JavaImportImpl(rendererUtilsClassName + ".Attributes"));
        this.runtimeImport.add(new JavaImportImpl(rendererUtilsClassName + ".ScriptHashVariableWrapper"));
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return Iterables.concat(super.getRequiredImports(), runtimeImport);
    }
}
