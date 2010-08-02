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

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.faces.render.RenderKitFactory;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.templatecompiler.RendererTemplateParser;
import org.richfaces.cdk.util.Strings;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Dec 30, 2009
 */
@SupportedAnnotationTypes({"javax.faces.component.FacesComponent", JsfRenderer.NAME})
public class RendererProcessor extends ProcessorBase implements CdkAnnotationProcessor {

    private static final String COMPONENT_FAMILY = "COMPONENT_FAMILY";

    private static final String RENDERER_TYPE = "RENDERER_TYPE";

    @Inject
    @Source(Sources.RENDERER_TEMPLATES)
    private FileManager sources;

    @Inject
    private RendererTemplateParser templateParser;

    public void process(Element rendererElement, ComponentLibrary library) {
        JsfRenderer annotation = rendererElement.getAnnotation(JsfRenderer.class);

        RendererModel rendererModel = process((TypeElement) rendererElement, annotation, library);

        addToRenderKit(annotation, library, rendererModel);

/*        ComponentModel componentModel = library.getComponentModel(rendererModel.getFamily());
        if (componentModel != null) {
            componentModel.getRenderers().add(rendererModel);
        } else {
            System.out.println("For renderer with type (" + rendererModel.getType() + ") and family ("
                    + rendererModel.getFamily() + ") comopnent was not found");
        }*/
    }

    public RendererModel process(TypeElement rendererElement, JsfRenderer annotation, ComponentLibrary library) {
        RendererModel rendererModel = new RendererModel();

        setClassNames(rendererElement, rendererModel, null);

        setRendererType(rendererElement, rendererModel, annotation);
        setComponentType(rendererModel);
        setComponentFamily(rendererElement, rendererModel, annotation);
        setDescription(rendererModel, annotation.description(), getDocComment(rendererElement));

        setTemplate(rendererModel, annotation);

//        processFacets(componentElement, renderer);
//        processEvents(componentElement, renderer);
        // TODO - process renderers ( @JsfRenderer and @RendererTemplate attribute.
        // TODO - process @Test annotations.
//        processAttributes(componentElement, component);
        return rendererModel;
    }

    public RendererModel process(JsfRenderer renderer, ComponentLibrary library) {
        RendererModel rendererModel = new RendererModel();
        return rendererModel;
    }

    protected void setClassNames(TypeElement componentElement, RendererModel modelElement, String generatedClass) {

        if (componentElement.getModifiers().contains(Modifier.ABSTRACT) || !Strings.isEmpty(generatedClass)) {
            modelElement.setGenerate(true);
            modelElement.setRendererClass(ClassName.parseName(generatedClass));
            modelElement.setBaseClass(ClassName.parseName(componentElement.getQualifiedName().toString()));
        } else {
            modelElement.setGenerate(false);
            modelElement.setRendererClass(ClassName.parseName(componentElement.getQualifiedName().toString()));
            
            TypeMirror superclass = componentElement.getSuperclass();
            if (superclass.getKind() == TypeKind.DECLARED) {
                TypeElement typeElement = getSourceUtils().asTypeElement(superclass);
                modelElement.setBaseClass(ClassName.parseName(typeElement.getQualifiedName().toString()));
            }
        }

    }

    private void setComponentType(RendererModel rendererModel) {
//        getNamingConventions().inferComponentType(rendererModel.getFamily());
    }

    private void addToRenderKit(JsfRenderer annotation, ComponentLibrary library, RendererModel rendererModel) {
        String renderKitId = annotation.renderKitId();
        if (Strings.isEmpty(renderKitId)) {
            renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT; // TODO ???
        }
        library.addRenderer(renderKitId, rendererModel);
    }

    private void setTemplate(RendererModel rendererModel, JsfRenderer annotation) {
        String template = annotation.template();
        if (!Strings.isEmpty(template)) {
            rendererModel.setTemplatePath(template);

            // TODO - add template to source list to process it in appropriate builder.
            processRendererTemplate(rendererModel);
        }
    }

    private void processRendererTemplate(RendererModel renderer) {
        String templatePath = renderer.getTemplatePath();
        try {

            File file = sources.getFile(templatePath);
            templateParser.build(file, renderer);
        } catch (FileNotFoundException e) {
            // TODO log
            e.printStackTrace();
        }
    }

    private void setComponentFamily(TypeElement rendererElement, RendererModel rendererModel, JsfRenderer annotation) {
        String family = annotation.family();
        if (!Strings.isEmpty(family)) {
            rendererModel.setFamily(FacesId.parseId(family));
            return;
        } else {

            Object value = getSourceUtils().getConstant(rendererElement, COMPONENT_FAMILY);
            if (value != null) {
                rendererModel.setFamily(FacesId.parseId(value.toString()));
                return;
            }
        }
    }

    private String getRendererType(TypeElement rendererElement, JsfRenderer annotation) {
        String type = annotation.type();
        if (!Strings.isEmpty(type)) {
            return type;
        }

        Object value = getSourceUtils().getConstant(rendererElement, RENDERER_TYPE);
        if (value != null) {
            return value.toString();
        }

        return null;
    }

    private void setRendererType(TypeElement rendererElement, RendererModel rendererModel, JsfRenderer annotation) {
        rendererModel.setId(FacesId.parseId(getRendererType(rendererElement, annotation)));
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
