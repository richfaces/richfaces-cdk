/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.templatecompiler.builder.model;

import java.util.regex.Pattern;

/**
 * @author Nick Belaevski
 *
 */
public class JavaImportImpl implements JavaImport {
    private static final Pattern JAVA_LANG_PATTERN = Pattern.compile("^java\\.lang\\.[^\\.]+$");
    private String name;
    private boolean staticImport;

    public JavaImportImpl(String name) {
        this(name, false);
    }

    public JavaImportImpl(Class<?> clazz) {
        this(clazz.getName());
    }

    public JavaImportImpl(String name, boolean staticImport) {
        super();
        this.name = name;
        this.staticImport = staticImport;
    }

    public JavaImportImpl(Class<?> clazz, boolean staticImport) {
        this(clazz.getName(), staticImport);
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return name.replace('$', '.');
    }

    public boolean isStatic() {
        return staticImport;
    }

    public boolean isDefault() {
        String name = getName();

        if (!name.contains(".")) {
            return true;
        }

        if (JAVA_LANG_PATTERN.matcher(name).matches()) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isStatic() ? 1231 : 1237);
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JavaImport other = (JavaImport) obj;
        if (isStatic() != other.isStatic()) {
            return false;
        }
        if (getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!getName().equals(other.getName())) {
            return false;
        }
        return true;
    }
}
