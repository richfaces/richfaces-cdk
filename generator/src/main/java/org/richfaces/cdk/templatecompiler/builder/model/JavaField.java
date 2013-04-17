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

import org.richfaces.cdk.templatecompiler.el.types.ELType;

import com.google.common.collect.Iterables;

/**
 * Class field abstraction
 *
 * @author Maksim Kaszynski
 */
public class JavaField extends JavaLanguageElement {
    private ELType type;
    private JavaStatement value;

    public JavaField(ELType type, String name) {
        this(type, name, null);
    }

    public JavaField(ELType type, String name, JavaStatement value) {
        super(name);
        this.type = type;
        this.value = value;
    }

    public ELType getType() {
        return type;
    }

    public JavaStatement getValue() {
        return value;
    }

    public void setValue(JavaStatement value) {
        this.value = value;
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        Iterable<JavaImport> imports = super.getRequiredImports();
        return Iterables.concat(getType().getRequiredImports(), imports);
    }
}
