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

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.EventModel;

/**
 * <p class="changed_added_4_0">
 * This class processes annotations for FacesEvents.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class EventProcessor extends ProcessorBase implements CdkAnnotationProcessor {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.apt.processors.CdkAnnotationProcessor#getProcessedAnnotation()
     */
    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return Event.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.apt.processors.CdkAnnotationProcessor#process(javax.lang.model.element.Element,
     * org.richfaces.cdk.model.ComponentLibrary)
     */
    @Override
    public void process(Element element, ComponentLibrary library) throws CdkProcessingException {
        if (ElementKind.CLASS.equals(element.getKind())) {
            SourceUtils sourceUtils = getSourceUtils();
            TypeElement eventType = (TypeElement) element;
            AnnotationMirror event = sourceUtils.getAnnotationMirror(element, Event.class);
            EventModel model = new EventModel();
            model.setType(ClassName.parseName(eventType.getQualifiedName().toString()));
            sourceUtils.setModelProperty(model, event, "listenerInterface", "listener");
            sourceUtils.setModelProperty(model, event, "listenerMethod");
            sourceUtils.setModelProperty(model, event, "sourceInterface", "source");
            setTagInfo(event, model);
            library.getEvents().add(model);
        }
    }
}
