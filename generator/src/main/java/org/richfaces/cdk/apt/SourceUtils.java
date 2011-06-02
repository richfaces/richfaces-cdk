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
package org.richfaces.cdk.apt;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.richfaces.cdk.model.ClassName;

import com.google.inject.ProvidedBy;

/**
 * <p class="changed_added_4_0">
 * This class provides utility methods to analayze java classes. This implementation uses APT API to get information about Java
 * code.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@ProvidedBy(SourceUtilsProvider.class)
public interface SourceUtils {
    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @author asmirnov@exadel.com
     *
     */
    public interface SuperTypeVisitor {
        void visit(TypeMirror type);
    }

    enum ACCESS_TYPE {
        readOnly,
        writeOnly,
        readWrite
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @author asmirnov@exadel.com
     *
     */
    public interface BeanProperty {
        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the name
         */
        String getName();

        /**
         * <p class="changed_added_4_0">
         * Get JavaDoc comment of appropriate bean property element.
         * </p>
         *
         * @return
         */
        String getDocComment();

        /**
         * <p class="changed_added_4_0">
         * Bean property type
         * </p>
         *
         * @return
         */
        ClassName getType();

        /**
         * <p class="changed_added_4_0">
         * Is this property implementted by component
         * </p>
         *
         * @return the exists
         */
        boolean isExists();

        boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

        AnnotationMirror getAnnotationMirror(Class<? extends Annotation> annotationType);

        <T extends Annotation> T getAnnotation(Class<T> annotationType);

        ACCESS_TYPE getAccessType();
    }

    /**
     * <p class="changed_added_4_0">
     * Get all fields and bean properties that are annotated with given annotation.
     * </p>
     *
     * @param annotation
     * @param type
     * @return
     */
    Set<BeanProperty> getBeanPropertiesAnnotatedWith(Class<? extends Annotation> annotation, TypeElement type);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param type
     * @return
     */
    Set<BeanProperty> getAbstractBeanProperties(TypeElement type);

    /**
     * <p class="changed_added_4_0">
     * Get bean property descriptor for particular type.
     * </p>
     *
     * @param type
     * @param name
     * @return
     */
    BeanProperty getBeanProperty(TypeElement type, String name);

    /**
     * <p class="changed_added_4_0">
     * Get bean property descriptor for particular type.
     * </p>
     *
     * @param type
     * @param name
     * @return
     */
    BeanProperty getBeanProperty(ClassName type, String name);

    /**
     * <p class="changed_added_4_0">
     * Get JavaDoc comments associated with given element.
     * </p>
     *
     * @param componentElement
     * @return
     */
    String getDocComment(Element element);

    /**
     * <p class="changed_added_4_0">
     * Check model element for presense of annotation.
     * </p>
     *
     * @param element
     * @param annotationType
     * @return
     */
    boolean isAnnotationPresent(Element element, Class<? extends Annotation> annotationType);

    /**
     * <p class="changed_added_4_0">
     * Get model representation of the annotation for given model element
     * </p>
     *
     * @param annotationType
     * @return
     */
    AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotationType);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param annotation
     * @param propertyName
     * @return
     */
    <T> T getAnnotationValue(AnnotationMirror annotation, String propertyName, Class<T> expectedType);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param annotation
     * @param propertyName
     * @return
     */
    <T> Iterable<T> getAnnotationValues(AnnotationMirror annotation, String propertyName, Class<T> expectedType);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param annotation
     * @param propertyName
     * @return
     */
    boolean isAnnotationPropertyPresent(AnnotationMirror annotation, final String propertyName);

    /**
     * <p class="changed_added_4_0">
     * Check annotation proprrty for default value.
     * </p>
     *
     * @param annotation
     * @param propertyName
     * @return true if property has its default value.
     */
    boolean isDefaultValue(AnnotationMirror annotation, String propertyName);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param model
     * @param annotation
     * @param modelProperty
     * @param annotationAttribute
     */
    void setModelProperty(Object model, AnnotationMirror annotation, String modelProperty, String annotationAttribute);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param model
     * @param annotation
     * @param modelProperty
     */
    void setModelProperty(Object model, AnnotationMirror annotation, String modelProperty);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param componentElement
     * @param name
     * @return
     */
    Object getConstant(TypeElement element, String name);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param type
     * @param visitor
     */
    void visitSupertypes(TypeElement type, SuperTypeVisitor visitor);

    /**
     * <p class="changed_added_4_0">
     * Converts TypeMirror into corresponding TypeElement
     * </p>
     *
     * @param mirror
     * @return The Element for given type
     */
    TypeElement asTypeElement(TypeMirror mirror);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param type
     * @return true if class already exist in project source or dependent libraries.
     */
    boolean isClassExists(ClassName type);
}
