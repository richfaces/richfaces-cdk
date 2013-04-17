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

/**
 * Representation of method argument
 *
 * @author Maksim Kaszynski
 */
public class Argument implements RequireImports {
    private String name;
    private ELType type;

    private boolean required;
    private String defaultValue;

    public Argument(String name, ELType type) {
        super();
        this.name = name;
        this.type = type;
    }

    public static Argument arg(String name, ELType type) {
        return new Argument(name, type);
    }

    public ELType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefault() {
        return defaultValue;
    }

    public void setRequired(Boolean required) {
        if (required == null) {
            required = false;
        }
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return type.getRequiredImports();
    }
}
