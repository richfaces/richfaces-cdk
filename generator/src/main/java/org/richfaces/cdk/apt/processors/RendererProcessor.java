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

package org.richfaces.cdk.apt.processors;

import java.lang.annotation.Annotation;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.util.Strings;


/**
 * @author akolonitsky
 * @since Dec 30, 2009
 */
@SupportedAnnotationTypes({"javax.faces.component.FacesComponent", JsfRenderer.NAME})
public class RendererProcessor extends ProcessorBase implements CdkAnnotationProcessor {

    private static final String COMPONENT_FAMILY = "COMPONENT_FAMILY";

    private static final String RENDERER_TYPE = "RENDERER_TYPE";
    
    public void process(Element rendererElement, ComponentLibrary library) {
        SourceUtils sourceUtils = getSourceUtils();
        AnnotationMirror annotation = sourceUtils.getAnnotationMirror(rendererElement, JsfRenderer.class);

        RendererModel rendererModel = new RendererModel();

        TypeElement rendererTypeElement = (TypeElement) rendererElement;
        setClassNames(rendererTypeElement, rendererModel, annotation);

        setRendererType(rendererTypeElement, rendererModel, annotation);
        
        setComponentFamily(rendererTypeElement, rendererModel, annotation);
        setDescription(rendererModel, annotation, getDocComment(rendererElement));

        sourceUtils.setModelProperty(rendererModel, annotation, "templatePath","template");

        String renderKitId = sourceUtils.getAnnotationValue(annotation, "renderKitId", String.class);
        library.addRenderer(renderKitId, rendererModel);

    }



    private void setTemplate(RendererModel rendererModel, JsfRenderer annotation) {
        String template = annotation.template();
        if (!Strings.isEmpty(template)) {
            rendererModel.setTemplatePath(template);
        }
    }


    private void setComponentFamily(TypeElement rendererElement, RendererModel rendererModel, AnnotationMirror annotation) {
        rendererModel.setFamily(FacesId.parseId(getAnnotationPropertyOrConstant(rendererElement, annotation,"family",COMPONENT_FAMILY)));
    }

    private void setRendererType(TypeElement rendererElement, RendererModel rendererModel, AnnotationMirror annotation) {
        rendererModel.setId(FacesId.parseId(getAnnotationPropertyOrConstant(rendererElement, annotation,"type",RENDERER_TYPE)));
    }

    protected String getComponentType(TypeElement componentElement) {
        JsfComponent annotation = componentElement.getAnnotation(JsfComponent.class);
        if (annotation != null) {
            return annotation.type();
        }

        return null;
    }

    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return JsfRenderer.class;
    }

    protected String[] getAnnotationAttributes(TypeElement componentElement) {
        return null;
    }
}
