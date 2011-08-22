/*
 * $Id: BehaviorRendererProcessor.java 20938 2011-01-10 22:35:38Z alexsmirnov $
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
import javax.lang.model.element.TypeElement;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.annotations.JsfBehaviorRenderer;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.BehaviorRendererModel;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class BehaviorRendererProcessor extends ProcessorBase implements CdkAnnotationProcessor {
    @Override
    public void process(Element element, ComponentLibrary library) throws CdkProcessingException {
        SourceUtils sourceUtils = getSourceUtils();
        AnnotationMirror behaviorRenderer = sourceUtils.getAnnotationMirror(element, JsfBehaviorRenderer.class);

        BehaviorRendererModel behaviorRendererModel = new BehaviorRendererModel();
        setClassNames((TypeElement) element, behaviorRendererModel, behaviorRenderer);
        sourceUtils.setModelProperty(behaviorRendererModel, behaviorRenderer, "id", "type");

        setDescription(behaviorRendererModel, behaviorRenderer, getDocComment(element));
        FacesId renderKitId = sourceUtils.getAnnotationValue(behaviorRenderer, "renderKitId", FacesId.class);
        library.addRenderKit(renderKitId).getBehaviorRenderers().add(behaviorRendererModel);
    }

    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return JsfBehaviorRenderer.class;
    }
}
