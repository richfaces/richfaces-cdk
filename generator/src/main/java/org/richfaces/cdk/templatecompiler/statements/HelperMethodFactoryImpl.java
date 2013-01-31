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

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import org.richfaces.cdk.Generator;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.builder.model.Argument;
import org.richfaces.cdk.templatecompiler.builder.model.JavaMethod;
import org.richfaces.cdk.templatecompiler.builder.model.JavaModifier;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class HelperMethodFactoryImpl implements HelperMethodFactory {
    private TypesFactory typesFactory;
    @Inject(optional = true)
    @Named(Generator.RENDERER_UTILS_CLASS)
    private String rendererUtilsClass = "org.richfaces.renderkit.RenderKitUtils";
    private FreeMarkerRenderer renderer;
    private final EnumMap<HelperMethod, JavaMethod> helperMethods = Maps.newEnumMap(HelperMethod.class);
    private final Logger log;

    @Inject
    public HelperMethodFactoryImpl(Logger log) {
        this.log = log;
    }

    /**
     * <p class="changed_added_4_0">
     * Initialization code. It should be called after field injection, so render kit utils class name should have proper value
     * here.
     * </p>
     *
     * @param renderer
     * @param typesFactory
     */
    @Inject
    public void initHelperMethods(@TemplateModel FreeMarkerRenderer renderer, TypesFactory typesFactory) {
        this.renderer = renderer;
        this.typesFactory = typesFactory;
        buildHelperMethod(HelperMethod.EMPTINESS_CHECK, false, "emptiness-check-method", "object");
        buildHelperMethod(HelperMethod.EQUALS_CHECK, false, "equals-check-method", "o1", "o2");
        buildHelperMethod(HelperMethod.TO_BOOLEAN_CONVERSION, false, "conversion-to-boolean-method", "object");
        buildHelperMethod(HelperMethod.TO_INTEGER_CONVERSION, false, "conversion-to-integer-method", "object");
        buildHelperMethod(HelperMethod.TO_STRING_CONVERSION, false, "conversion-to-string-method", "object");
        buildHelperMethod(HelperMethod.SHOULD_RENDER_ATTRIBUTE, true, "should-render-attribute", "attributeValue");
        buildHelperMethod(HelperMethod.RENDER_ATTRIBUTE, true, "render-attribute", "attributeValue").getExceptions().add(
                typesFactory.getType(IOException.class));
        JavaMethod renderAttributes = buildHelperMethod(HelperMethod.RENDER_ATTRIBUTES_SET, true, "render-attributes-set",
                "context", "component");
        // TODO - put unknown 'Attributes' class into HelperMethod.
        List<Argument> arguments = renderAttributes.getArguments();
        arguments.add(new Argument("attributes", typesFactory.getType("Attributes")));
        renderAttributes.getExceptions().add(typesFactory.getType(IOException.class));
        buildHelperMethod(HelperMethod.CREATE_ATTRIBUTES, true, "create-attributes");
        buildHelperMethod(HelperMethod.ADD_TO_SCRIPT_HASH, true, null);
        buildHelperMethod(HelperMethod.ADD_TO_SCRIPT_HASH_ATTRIBUTES, true, null);
        buildHelperMethod(HelperMethod.TO_SCRIPT_ARGS, true, null);
        buildHelperMethod(HelperMethod.CONCAT, true, null);
        buildHelperMethod(HelperMethod.HAS_FACET, true, null);
    }

    private JavaMethod buildHelperMethod(HelperMethod helperMethod, boolean utilsMethod, String templateName,
            String... argumentNames) {
        JavaMethod helperJavaMethod;
        if (utilsMethod && !RendererUtilsMethod.BUILT_IN.equals(rendererUtilsClass)) {
            helperJavaMethod = new RendererUtilsMethod(helperMethod, rendererUtilsClass);
        } else {
            String[] argumentTypes = helperMethod.getArgumentTypes();
            Argument[] arguments = new Argument[argumentNames.length];
            for (int i = 0; i < argumentNames.length; i++) {
                arguments[i] = new Argument(argumentNames[i], typesFactory.getType(argumentTypes[i]));
            }
            ELType returnType = typesFactory.getType(helperMethod.getReturnType());
            helperJavaMethod = new JavaMethod(helperMethod.getName(), returnType, arguments);
            helperJavaMethod.addModifier(JavaModifier.PRIVATE);
            helperJavaMethod.addModifier(JavaModifier.STATIC);
            helperJavaMethod.setMethodBody(new FreeMarkerTemplateStatementBase(renderer, templateName));
        }
        helperMethods.put(helperMethod, helperJavaMethod);
        return helperJavaMethod;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.statements.HelperMethodFactory#getHelperMethod(org.richfaces.cdk.templatecompiler
     * .el.HelperMethod)
     */
    @Override
    public JavaMethod getHelperMethod(HelperMethod helper) {
        return helperMethods.get(helper);
    }
}
