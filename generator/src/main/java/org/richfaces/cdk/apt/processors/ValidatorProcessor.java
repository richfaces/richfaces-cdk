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

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.annotations.JsfValidator;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ValidatorModel;

/**
 * @author akolonitsky
 * @since Jan 13, 2010
 */
@SupportedAnnotationTypes({"javax.faces.component.FacesComponent", JsfValidator.NAME})
public class ValidatorProcessor extends ProcessorBase implements CdkAnnotationProcessor {

    @Override
    public void process(Element element, ComponentLibrary library) throws CdkProcessingException {
        SourceUtils utils = getSourceUtils();
        AnnotationMirror validator = utils.getAnnotationMirror(element,JsfValidator.class);

        ValidatorModel validatorModel = new ValidatorModel();
        utils.setModelProperty(validatorModel, validator, "id");
        setClassNames((TypeElement) element, validatorModel, validator);
        setDescription(validatorModel, validator, getDocComment(element));

        setTagInfo(validator, validatorModel);

        library.getValidators().add(validatorModel);
    }

    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return JsfValidator.class;
    }
}
