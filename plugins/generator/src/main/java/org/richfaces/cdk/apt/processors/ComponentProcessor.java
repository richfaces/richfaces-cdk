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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.faces.event.FacesEvent;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.RendererSpecificComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.apt.SourceUtils.BeanProperty;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.FacetModel;
import org.richfaces.cdk.util.Strings;

import com.google.inject.Inject;

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

    @Inject
    private RendererProcessor rendererProcessor;

    public void process(Element componentElement, ComponentLibrary library) {
        final JsfComponent annotation = componentElement.getAnnotation(JsfComponent.class);
        if (annotation == null) {
            return;
        }

        // Process class-level annotations.
        ComponentModel component = new ComponentModel();

        // Should that component be generated ?
        setClassNames((TypeElement) componentElement, component, annotation.generate());
        setComponentProperties((TypeElement) componentElement, component, annotation);
        library.getComponents().add(component);

        // Process the second level annotations.
        for (final RendererSpecificComponent subcomponent : annotation.components()) {
            JsfComponent subAnnotation = new JsfSubComponent(subcomponent, annotation);
            ComponentModel subcomponentModel = new ComponentModel();
            subcomponentModel.setBaseClass(component.getTargetClass());
            subcomponentModel.setTargetClass(ClassName.parseName(subcomponent.generate()));
            subcomponentModel.setGenerate(!Strings.isEmpty(subcomponent.generate()));
            setComponentProperties(null, subcomponentModel, subAnnotation);
            library.getComponents().add(subcomponentModel);
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
    void setComponentProperties(TypeElement componentElement, ComponentModel component, JsfComponent annotation)
        throws CdkException {

        setComponentType(componentElement, component, annotation.type());
        setComponeneFamily(componentElement, component, annotation.family());
        setDescription(component, annotation.description(), getDocComment(componentElement));
        setRendererType(component, annotation.renderer());

        processFacets(componentElement, component, annotation);
        processAttributes(componentElement, component, annotation);
        processEvents(componentElement, component, annotation);
        for (Tag tag : annotation.tag()) {
            setTagInfo(tag, component);
        }

        // TODO - process @Test annotations.
    }

    private void setRendererType(ComponentModel component, JsfRenderer jsfRenderer) {
        component.setRendererType(FacesId.parseId(jsfRenderer.type()));
    }

    private void processAttributes(TypeElement componentElement, ComponentModel component, JsfComponent annotation) {
        AttributesProcessor attributesProcessor = getAttributeProcessor();
        attributesProcessor.processXmlFragment(component, annotation.attributes());

        if (componentElement != null) {
            attributesProcessor.processType(component, componentElement);
        }

        Collection<ClassName> interfaceNames = component.getInterfaces();
        try {
            Class<?>[] interfaces = annotation.interfaces();
            for (Class<?> clazz : interfaces) {
                interfaceNames.add(new ClassName(clazz));
            }
        } catch (MirroredTypesException e) {
            List<? extends TypeMirror> typeMirrors = e.getTypeMirrors();
            for (TypeMirror mirror : typeMirrors) {
                processInterface(component, attributesProcessor, mirror);
            }
        } catch (MirroredTypeException e) {
            processInterface(component, attributesProcessor, e.getTypeMirror());
        }
    }

    private void processInterface(ComponentModel component, AttributesProcessor attributesProcessor, TypeMirror mirror) {
        component.getInterfaces().add(ClassName.parseName(mirror.toString()));
        if (TypeKind.DECLARED.equals(mirror.getKind())) {
            attributesProcessor.processType(component, getSourceUtils().asTypeElement(mirror));
        } else {
            // TODO - record warning.
        }
    }

    void setComponentType(TypeElement componentElement, ComponentModel component, String type) {
        if (!Strings.isEmpty(type)) {
            component.setId(FacesId.parseId(type));
        } else if (null != componentElement) {

            // static final String COMPONENT_FAMILY = "...";
            Object value = getSourceUtils().getConstant(componentElement, COMPONENT_TYPE);
            if (value != null) {
                component.setId(FacesId.parseId(value.toString()));
            }
        }
    }

    final void processFacets(TypeElement componentElement, ComponentModel component, JsfComponent annotation) {
        if (null != componentElement) {
            SourceUtils sourceUtils = getSourceUtils();
            Set<BeanProperty> properties = sourceUtils.getBeanPropertiesAnnotatedWith(Facet.class, componentElement);

            // TODO - encapsulate attribute builder into utility class.
            for (BeanProperty beanProperty : properties) {
                Facet facet = beanProperty.getAnnotation(Facet.class);
                FacetModel facetModel = component.getOrCreateFacet(beanProperty.getName());

                facetModel.setDescription(beanProperty.getDocComment());

                processFacet(facet, facetModel, beanProperty.getDocComment());
                if (!beanProperty.isExists()) {
                    facetModel.setGenerate(true);
                }
            }

        }
        for (Facet facet : annotation.facets()) {
            String name = facet.name();
            if (!Strings.isEmpty(name)) {
                FacetModel facetModel = component.getOrCreateFacet(name);
                processFacet(facet, facetModel, null);
            } else {
                // TODO - record error.
                throw new CdkException("Facet name should be set");
            }
        }
    }

    final void processFacet(Facet facet, FacetModel facetModel, String docComment) {
        setDescription(facetModel, facet.description(), docComment);
        facetModel.setGenerate(facet.generate());
    }

    final void setComponeneFamily(TypeElement componentElement, ComponentModel component, String family) {
        if (!Strings.isEmpty(family)) {
            component.setFamily(FacesId.parseId(family));
        } else if (null != componentElement) {
            // static final String COMPONENT_FAMILY = "...";
            Object value = getSourceUtils().getConstant(componentElement, COMPONENT_FAMILY);
            if (null != value) {
                component.setFamily(FacesId.parseId(value.toString()));
            }
        }
    }

    final void processEvents(TypeElement componentElement, ComponentModel component, JsfComponent annotation) {
        try {
            Class<? extends FacesEvent>[] fires = annotation.fires();

            for (Class<? extends FacesEvent> event : fires) {
                try {
                    component.addEvent(event.getName());
                } catch (MirroredTypesException mirror) {
                    for (TypeMirror eventType : mirror.getTypeMirrors()) {
                        component.addEvent(eventType.toString());
                    }
                } catch (MirroredTypeException mirror) {
                    component.addEvent(mirror.getTypeMirror().toString());
                }

            }
        } catch (MirroredTypesException mirror) {
            for (TypeMirror eventType : mirror.getTypeMirrors()) {
                component.addEvent(eventType.toString());
            }
        } catch (MirroredTypeException mirror) {
            component.addEvent(mirror.getTypeMirror().toString());
        }
    }

    @Override
    public final Class<? extends Annotation> getProcessedAnnotation() {
        return JsfComponent.class;
    }
}
