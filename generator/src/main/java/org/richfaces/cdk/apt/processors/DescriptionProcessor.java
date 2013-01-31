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

import javax.lang.model.element.AnnotationMirror;

import org.richfaces.cdk.model.DescriptionGroup;

/**
 * <p class="changed_added_4_0">
 * Implementation of that interface process {@link Description} annotation and set information from it into model.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface DescriptionProcessor {
    /**
     * <p class="changed_added_4_0">
     * process {@link Description} annotation and set information from it into model. Optional string from the JavaDoc comment
     * has precedence over {@link Desription#overwrite()} attribute.
     * </p>
     *
     * @param model
     * @param annotation
     * @param docComment JavaDoc comment associated with described element.
     */
    void processDescription(DescriptionGroup model, AnnotationMirror annotation, String docComment);

    /**
     * <p class="changed_added_4_0">
     * process {@link Description} annotation and set information from it into model.
     * </p>
     *
     * @param model
     * @param description
     */
    void processDescription(DescriptionGroup model, AnnotationMirror description);
}
