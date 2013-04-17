/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.Fragment;
import org.richfaces.cdk.templatecompiler.FragmentStore;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.builder.model.Argument;
import org.richfaces.cdk.templatecompiler.el.ParsingException;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * @author Lukas Fryc
 */
public class RenderFragmentStatement extends FreeMarkerTemplateStatementBase {

    private ELParser parser;
    private Logger logger;

    private String methodName;
    private Map<QName, String> attributes;
    private boolean extendingRendererBase;
    private List<String> arguments;
    private FragmentStore fragmentStore;

    @Inject
    public RenderFragmentStatement(@TemplateModel FreeMarkerRenderer renderer, ELParser parser, Logger logger) {
        super(renderer, "render-fragment");

        this.parser = parser;
        this.logger = logger;
    }

    public List<String> getArguments() {
        if (arguments == null) {
            arguments = Lists.newLinkedList();

            final Fragment fragment = fragmentStore.getFragment(methodName);

            for (Argument argument : fragment.getAllArguments()) {
                String argumentValue = getAttributeValue(argument);
                arguments.add(argumentValue);
            }
        }

        return arguments;
    }

    private String getAttributeValue(Argument argument) {
        final String argumentName = argument.getName();
        if (attributes != null) {
            for (Entry<QName, String> entry : attributes.entrySet()) {
                QName qname = entry.getKey();
                String attributeName = qname.getLocalPart();
                if (argumentName.equals(attributeName)) {
                    String valueExpression = entry.getValue();
                    return parseValueExpression(argument, valueExpression);
                }
            }
        }

        // no associated attribute in cdk:renderFragment was found

        if (argument.isRequired()) {
            throw new IllegalStateException("Missing value for required attribute '" + argumentName + "'");
        }

        if (argument.getDefault() != null) {
            String valueExpression = argument.getDefault();
            return parseValueExpression(argument, valueExpression);
        }

        return "null";
    }

    private String parseValueExpression(Argument argument, String valueExpression) {
        try {
            TypedTemplateStatement statement = parser.parse(valueExpression, this, argument.getType());
            String code = statement.getCode();

            this.addRequiredMethods(statement.getRequiredMethods());

            return code;
        } catch (ParsingException e) {
            logger.error("Error parse renderFragment argument " + argument.getName() + " expression: " + valueExpression, e);
            return valueExpression;
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isExtendingRendererBase() {
        return extendingRendererBase;
    }

    public void setExtendingRendererBase(boolean extendingRendererBase) {
        this.extendingRendererBase = extendingRendererBase;
    }

    public Map<QName, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<QName, String> attributes) {
        this.attributes = attributes;
    }

    public void setFragmentStore(FragmentStore fragmentStore) {
        this.fragmentStore = fragmentStore;
    }
}
