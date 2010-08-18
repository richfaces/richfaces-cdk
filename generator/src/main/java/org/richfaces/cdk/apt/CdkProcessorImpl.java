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
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.LibraryBuilder;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.ModelBuilder;
import org.richfaces.cdk.ModelValidator;
import org.richfaces.cdk.apt.processors.CdkAnnotationProcessor;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * Base class for all CDK Annotation processors. That class provides access to current CDK context and utility methods
 * for Java source models.
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class CdkProcessorImpl extends AbstractProcessor implements CdkProcessor {

    private static final Set<String> PROCESSED_ANNOTATION = Collections.singleton("*");

    @Inject
    private Logger log;

    @Inject
    private Set<CdkAnnotationProcessor> processors;

    // TODO - set library as parameter.
    @Inject
    private ComponentLibrary library;

    @Inject
    private Set<ModelBuilder> builders;

    @Inject
    private SourceUtilsProvider sourceUtilsProducer;

    @Inject
    private ModelValidator validator;

    @Inject
    private LibraryBuilder builder;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        sourceUtilsProducer.setProcessingEnv(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            // Process annotations.
            for (CdkAnnotationProcessor process : processors) {
                processAnnotation(process, roundEnv);
            }
        } else  {
            // parse non-java sources
            processNonJavaSources();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.richfaces.cdk.apt.CdkProcessor#processNonJavaSources()
     */
    public void processNonJavaSources() {
        for (ModelBuilder builder : builders) {
            log.debug("Run builder "+builder.getClass().getName());
            builder.build();
        }
        // validator should be called even if previvous phases finish with errors, to collect all possible problems.
        log.debug("Validate model");
        validator.verify(library);
        if(0 == log.getErrorCount()) {
            // processing over, generate files.
            log.debug("Generate output files");
            builder.generate(library);
        }

    }

    protected void processAnnotation(CdkAnnotationProcessor processor, RoundEnvironment environment) {
        Class<? extends Annotation> processedAnnotation = processor.getProcessedAnnotation();
        log.debug("Process all elements annotated with "+processedAnnotation.getName());
        Target target = processedAnnotation.getAnnotation(Target.class);
        try {
            Set<? extends Element> rootElements = environment.getRootElements();
            for (Element element : rootElements) {
                if (isAppropriateTarget(element, target)){
                    processElement(processor, processedAnnotation, element);
                } else {
                    for (Element enclosedElement : element.getEnclosedElements()) {
                        if (isAppropriateTarget(enclosedElement, target)){
                            processElement(processor, processedAnnotation, enclosedElement);
                        }
                    }
                }
            }
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Kind.ERROR,
                "Errorr processing annotation " + processedAnnotation + ": " + e);
        }
    }

    private void processElement(CdkAnnotationProcessor processor, Class<? extends Annotation> processedAnnotation,
        Element element) {
        if (null != element.getAnnotation(processedAnnotation)) {
            try {
                log.debug("Process "+element.getSimpleName()+" annotated with "+processedAnnotation.getName());
                processor.process(element, library);
            } catch (CdkProcessingException e) {
                sendError(element, e);
            }
        }
    }
    
    private boolean isAppropriateTarget(Element element,Target target){
        boolean match = false;
        ElementKind kind = element.getKind();
        if(null != target){
            for(ElementType targetType : target.value()){
                switch (targetType) {
                    case TYPE:
                        match |= ElementKind.CLASS.equals(kind)||ElementKind.INTERFACE.equals(kind)||ElementKind.ENUM.equals(kind);
                        break;
                    case PACKAGE:
                        match |= ElementKind.PACKAGE.equals(kind);
                        break;
                    case METHOD:
                        match |= ElementKind.METHOD.equals(kind);
                        break;
                    case FIELD:
                        match |= ElementKind.FIELD.equals(kind);
                        break;
                    default:
                        break;
                }
            }
        } else {
            // Annotation without @Target match any element.
            match =
                ElementKind.CLASS.equals(kind) || ElementKind.INTERFACE.equals(kind) || ElementKind.ENUM.equals(kind)
                    || ElementKind.PACKAGE.equals(kind) || ElementKind.METHOD.equals(kind)
                    || ElementKind.FIELD.equals(kind);
        }
        return match;
    }

    protected void sendError(Element componentElement, CdkProcessingException e) {
        // rise error and continue.
        processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, e.getMessage(), componentElement);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return PROCESSED_ANNOTATION;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {

        // CDK supports Java 5 or 6 source code.
        return SourceVersion.RELEASE_6;
    }

}
