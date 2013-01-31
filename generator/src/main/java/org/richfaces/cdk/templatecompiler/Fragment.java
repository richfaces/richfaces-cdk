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
package org.richfaces.cdk.templatecompiler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.richfaces.cdk.templatecompiler.builder.model.Argument;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.model.CdkFragmentElement;
import org.richfaces.cdk.templatecompiler.model.CompositeAttribute;
import org.richfaces.cdk.templatecompiler.model.CompositeFragmentInterface;

import com.google.common.collect.Maps;

/**
 * Holds parameters of cdk:fragment
 *
 * @author Lukas Fryc
 */
public class Fragment {

    private String methodName;

    private Map<String, Argument> arguments = Maps.newLinkedHashMap();

    public Fragment(CdkFragmentElement fragmentElement, TypesFactory typesFactory) {
        methodName = fragmentElement.getName();

        CompositeFragmentInterface interfaze = fragmentElement.getFragmentInterface();
        List<CompositeAttribute> attributes = interfaze != null ? interfaze.getAttributes() : null;

        if (attributes != null) {

            for (CompositeAttribute attribute : attributes) {
                String name = attribute.getName();
                String typeName = attribute.getType();
                boolean required = attribute.isRequired();
                String defaultValue = attribute.getDefault();

                ELType type = getType(name, typeName, typesFactory);

                Argument argument = new Argument(name, type);
                argument.setDefault(defaultValue);
                argument.setRequired(required);
                arguments.put(name, argument);
            }
        }
    }

    private ELType getType(String attributeName, String typeName, TypesFactory typesFactory) {
        try {
            return typesFactory.getType(typeName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot determine type for attribute '" + attributeName + "' of type '"
                    + typeName + "'");
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public Argument getArgumentByName(String argumentName) {
        return arguments.get(argumentName);
    }

    public Collection<Argument> getAllArguments() {
        return Collections.unmodifiableCollection(arguments.values());
    }
}
