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
import javax.lang.model.element.TypeElement;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.apt.SourceUtils.BeanProperty;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.FacetModel;

/**
 * <p class="changed_added_4_0">
 * That class process component-related annotations such as {@link org.richfaces.cdk.annotations.JsfComponent} or
 * {@link javax.faces.component.FacesComponent} and stores information in model.
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class ComponentProcessor extends ProcessorBase implements CdkAnnotationProcessor {

    public static final String COMPONENT_FAMILY = "COMPONENT_FAMILY";

    public static final String COMPONENT_TYPE = "COMPONENT_TYPE";

    public void process(Element componentElement, ComponentLibrary library) {
        SourceUtils sourceUtils = getSourceUtils();
        if (sourceUtils.isAnnotationPresent(componentElement, JsfComponent.class)) {
            AnnotationMirror annotation = sourceUtils.getAnnotationMirror(componentElement, JsfComponent.class);
            // Process class-level annotations.
            ComponentModel component = new ComponentModel();

            // Should that component be generated ?
            setClassNames((TypeElement) componentElement, component, annotation);
            setComponentProperties((TypeElement) componentElement, component, annotation);

            library.getComponents().add(component);

            // Process the second level annotations.
            for (AnnotationMirror subcomponent : sourceUtils.getAnnotationValues(annotation, "components",
                AnnotationMirror.class)) {
                ComponentModel subcomponentModel = new ComponentModel();
                subcomponentModel.setBaseClass(component.getTargetClass());
                if (!sourceUtils.isDefaultValue(subcomponent, "generate")) {
                    subcomponentModel.setTargetClass(sourceUtils.getAnnotationValue(subcomponent, "generate",
                        ClassName.class));
                    subcomponentModel.setGenerate(true);
                }
                setComponentProperties(null, subcomponentModel, subcomponent);
                library.getComponents().add(subcomponentModel);
            }
        }
    }

    /**
     * <p class="changed_added_4_0">
     * process annotation and set component model properties.
     * </p>
     * 
     * @param componentElement
     * @param component
     * @param annotation
     * @throws CdkException
     */
    void setComponentProperties(TypeElement componentElement, ComponentModel component, AnnotationMirror annotation)
        throws CdkException {
        SourceUtils sourceUtils = getSourceUtils();

        setComponentType(componentElement, component, annotation);
        setComponeneFamily(componentElement, component, annotation);
        setDescription(component, annotation, getDocComment(componentElement));
        if (!sourceUtils.isDefaultValue(annotation, "renderer")) {
            setRendererType(component, sourceUtils.getAnnotationValue(annotation, "renderer", AnnotationMirror.class));
        }
        processFacets(componentElement, component, annotation);
        processAttributes(componentElement, component, annotation);
        processEvents(componentElement, component, annotation);
        setTagInfo(annotation, component);

        // TODO - process @Test annotations.
    }

    private void setRendererType(ComponentModel component, AnnotationMirror annotation) {
        SourceUtils sourceUtils = getSourceUtils();
        if (!sourceUtils.isDefaultValue(annotation, "type")) {
            component.setRendererType(sourceUtils.getAnnotationValue(annotation, "type", FacesId.class));
        }
        if (!sourceUtils.isDefaultValue(annotation, "template")) {
            component.setRendererTemplate(sourceUtils.getAnnotationValue(annotation, "template", String.class));
        }
    }

    void setComponentType(TypeElement componentElement, ComponentModel component, AnnotationMirror annotation) {
        component.setId(FacesId.parseId(getAnnotationPropertyOrConstant(componentElement, annotation,"type",COMPONENT_TYPE)));
    }

    final void processFacets(TypeElement componentElement, ComponentModel component, AnnotationMirror annotation) {
        SourceUtils sourceUtils = getSourceUtils();
        if (null != componentElement) {
            Set<BeanProperty> properties = sourceUtils.getBeanPropertiesAnnotatedWith(Facet.class, componentElement);

            // TODO - encapsulate attribute builder into utility class.
            for (BeanProperty beanProperty : properties) {
                AnnotationMirror facet = beanProperty.getAnnotationMirror(Facet.class);
                FacetModel facetModel = component.getOrCreateFacet(beanProperty.getName());
                facetModel.setDescription(beanProperty.getDocComment());
                processFacet(facet, facetModel, beanProperty.getDocComment());
                if (!beanProperty.isExists()) {
                    facetModel.setGenerate(true);
                }
            }

        }
        for (AnnotationMirror facet : sourceUtils.getAnnotationValues(annotation, "facets", AnnotationMirror.class)) {
            if (!sourceUtils.isDefaultValue(facet, "name")) {
                String name = sourceUtils.getAnnotationValue(facet, "name", String.class);
                FacetModel facetModel = component.getOrCreateFacet(name);
                processFacet(facet, facetModel, null);
            } else {
                throw new CdkException("Facet name should be set");
            }
        }
    }

    final void processFacet(AnnotationMirror facet, FacetModel facetModel, String docComment) {
        SourceUtils sourceUtils = getSourceUtils();
        if (!sourceUtils.isDefaultValue(facet, "description")) {
            setDescription(facetModel, facet, docComment);
        }
        if (!sourceUtils.isDefaultValue(facet, "generate")) {
            facetModel.setGenerate(sourceUtils.getAnnotationValue(facet, "generate", Boolean.class));
        }
    }

    final void setComponeneFamily(TypeElement componentElement, ComponentModel component, AnnotationMirror annotation) {
        if(null != componentElement){
            component.setFamily(FacesId.parseId(getAnnotationPropertyOrConstant(componentElement, annotation,"family",COMPONENT_FAMILY)));
        }
    }

    final void processEvents(TypeElement componentElement, ComponentModel component, AnnotationMirror annotation) {
        SourceUtils sourceUtils = getSourceUtils();
        for (AnnotationMirror event : sourceUtils.getAnnotationValues(annotation, "fires", AnnotationMirror.class)) {
            EventModel model = new EventModel();
            sourceUtils.setModelProperty(model, event, "type", "value");
            sourceUtils.setModelProperty(model, event, "listenerInterface", "listener");
            sourceUtils.setModelProperty(model, event, "listenerMethod");
            sourceUtils.setModelProperty(model, event, "sourceInterface", "source");
            component.getEvents().add(model);
        }
    }

    @Override
    public final Class<? extends Annotation> getProcessedAnnotation() {
        return JsfComponent.class;
    }
}
