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
package org.richfaces.cdk.apt.processors;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.annotations.Function;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FunctionModel;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class FunctionProcessor extends ProcessorBase implements CdkAnnotationProcessor {
    private static final Joiner PARAMETERS_JOINER = Joiner.on(',').skipNulls();
    private static final com.google.common.base.Function<VariableElement, String> PARAMETER_CONVERTER = new com.google.common.base.Function<VariableElement, String>() {
        @Override
        public String apply(VariableElement var) {
            return var.asType().toString();
        }
    };

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.apt.processors.CdkAnnotationProcessor#getProcessedAnnotation()
     */
    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return Function.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.apt.processors.CdkAnnotationProcessor#process(javax.lang.model.element.Element,
     * org.richfaces.cdk.model.ComponentLibrary)
     */
    @Override
    public void process(Element element, ComponentLibrary library) throws CdkProcessingException {
        SourceUtils utils = getSourceUtils();
        switch (element.getKind()) {
            case METHOD:
                ExecutableElement methodElement = (ExecutableElement) element;
                // Only public static methods can be registered as functions.
                // TODO - move to validator.
                Set<Modifier> modifiers = methodElement.getModifiers();
                if (!modifiers.contains(Modifier.PUBLIC)) {
                    throw new CdkProcessingException("Only public method can be registered as EL function "
                            + methodElement.getSimpleName());
                }
                if (!modifiers.contains(Modifier.STATIC)) {
                    throw new CdkProcessingException("Only static method can be registered as EL function "
                            + methodElement.getSimpleName());
                }
                AnnotationMirror function = utils.getAnnotationMirror(methodElement, Function.class);
                FunctionModel model = new FunctionModel();
                if (!utils.isDefaultValue(function, "name")) {
                    utils.setModelProperty(model, function, "name");
                } else {
                    model.setName(methodElement.getSimpleName().toString());
                }
                model.setType(utils.getAnnotationValue(function, "type", TagType.class));
                setDescription(model, function, getDocComment(methodElement));
                // Calculate method signature
                StringBuilder signature = new StringBuilder();
                signature.append(methodElement.getReturnType()).append(" ");
                signature.append(methodElement.getSimpleName()).append("(");
                PARAMETERS_JOINER.appendTo(signature, Iterables.transform(methodElement.getParameters(), PARAMETER_CONVERTER));
                signature.append(")");
                model.setSignature(signature.toString());
                Element declaringClass = methodElement.getEnclosingElement();
                if (ElementKind.CLASS.equals(declaringClass.getKind())) {
                    model.setFunctionClass(ClassName.parseName(((TypeElement) declaringClass).getQualifiedName().toString()));
                }
                library.getFunctions().add(model);
                break;
            case CLASS:
                // TODO - process all public static methods in the class.
                break;

            default:
                break;
        }
    }
}
