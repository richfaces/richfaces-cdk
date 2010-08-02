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

import java.util.regex.Pattern;

/**
 * Implementation of import taht cannot be resolved in design time
 *
 * @author Maksim Kaszynski
 */
public class RuntimeImport implements JavaImport {
    
    private static final Pattern JAVA_LANG_PATTERN = Pattern.compile("^java\\.lang\\.[^\\.]+$");

    private String name;

    public RuntimeImport(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public boolean isDefault() {
        // default package & primitive types
        if (name.indexOf('.') == -1) {
            return true;
        }

        if (JAVA_LANG_PATTERN.matcher(name).matches()) {
            return true;
        }

        return false;
    }
}
