/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.cdk.templatecompiler.el.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.richfaces.cdk.util.ArrayUtils;

import com.google.inject.Inject;

/**
 * Provides functionality to parse the name of the class and infer the simple class name, type arguments and decide if type is
 * array.
 *
 * @author Lukas Fryc
 */
public class TypeParser {

    private static final Pattern CLASS_SIGNATURE_PATTERN = Pattern.compile("^" + "\\s*([^\\[<]+)\\s*" + // class name
            "(?:<\\s*(.*)\\s*>)?\\s*" + // generic signature
            "((?:\\[\\s*\\]\\s*)+)?\\s*" + // array signature
            "$");
    private static final int CLASS_NAME_GROUP_IDX = 1;
    private static final int TYPE_ARGUMENTS_GROUP_IDX = 2;
    private static final int ARRAY_SIGNATURE_GROUP_IDX = 3;
    private static final int ARRAY_SIGNATURE_LENGTH = "[]".length();

    @Inject
    TypesFactory typesFactory;

    private Matcher matcher;

    public TypeParser(String typeString) {
        matcher = CLASS_SIGNATURE_PATTERN.matcher(typeString);
    }

    public boolean isParseable() {
        return matcher.matches();
    }

    public ELType[] getTypeArguments() {
        String typeArgumentsString = matcher.group(TYPE_ARGUMENTS_GROUP_IDX);
        return parseTypeArgumentsString(typeArgumentsString);
    }

    private ELType[] parseTypeArgumentsString(String typeArguments) {
        if (typeArguments == null) {
            return PlainClassType.NO_TYPES;
        }

        String[] typeArgumentsSplit = typeArguments.trim().split(",");

        ELType[] types = new ELType[typeArgumentsSplit.length];
        for (int i = 0; i < typeArgumentsSplit.length; i++) {
            types[i] = typesFactory.getType(typeArgumentsSplit[i]);
        }

        return types;
    }

    public boolean isArray() {
        return getArrayDepth() != 0 || !ArrayUtils.isEmpty(getTypeArguments());
    }

    public int getArrayDepth() {
        String arraySignature = matcher.group(ARRAY_SIGNATURE_GROUP_IDX);
        int arrayDepth = 0;
        if (arraySignature != null) {
            arrayDepth = arraySignature.replaceAll("\\s+", "").length() / ARRAY_SIGNATURE_LENGTH;
        }
        return arrayDepth;
    }

    public String getClassName() {
        return matcher.group(CLASS_NAME_GROUP_IDX).trim();
    }

}