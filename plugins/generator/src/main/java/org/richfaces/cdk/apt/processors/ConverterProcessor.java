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
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.annotations.JsfConverter;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConverterModel;
import org.richfaces.cdk.model.FacesId;

/**
 * @author akolonitsky
 * @since Jan 4, 2010
 */
@SupportedAnnotationTypes({"javax.faces.component.FacesComponent", JsfConverter.NAME})
public class ConverterProcessor extends ProcessorBase implements CdkAnnotationProcessor {

    @Override
    public void process(Element element, ComponentLibrary library) throws CdkProcessingException {
        JsfConverter converter = element.getAnnotation(JsfConverter.class);

        ConverterModel converterModel = new ConverterModel();
        converterModel.setId(FacesId.parseId(converter.id()));

        try {
            Class<?> forClass = converter.forClass();
            if (!JsfConverter.NONE.class.equals(forClass)) {
                converterModel.setConverterForClass(new ClassName(forClass.getName()));
            }
        } catch (MirroredTypeException e) {
            String name = e.getTypeMirror().toString();
            if (!JsfConverter.NONE.class.getName().equals(name)) {
                converterModel.setConverterForClass(new ClassName(name));
            }
        }

        setDescription(converterModel, converter.description(), getDocComment(element));
        
        AttributesProcessor attributesProcessor = getAttributeProcessor();
        attributesProcessor.processXmlFragment(converterModel, converter.attributes());
        attributesProcessor.processType(converterModel, (TypeElement) element);
        setClassNames((TypeElement) element, converterModel, converter.generate());

        setTagInfo(converter.tag(), converterModel);

        library.getConverters().add(converterModel);
    }

    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return JsfConverter.class;
    }


    protected String[] getAnnotationAttributes(TypeElement element) {
        JsfConverter converter = element.getAnnotation(JsfConverter.class);

        return converter.attributes();
    }
}
