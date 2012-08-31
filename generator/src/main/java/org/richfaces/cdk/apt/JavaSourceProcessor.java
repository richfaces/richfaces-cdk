package org.richfaces.cdk.apt;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.apt.processors.CdkAnnotationProcessor;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;

public class JavaSourceProcessor {

    @Inject
    private Logger log;
    @Inject
    private Set<CdkAnnotationProcessor> processors;

    @Inject
    private ComponentLibrary library;

    @Inject
    private JavaSourceTracker sourceCache;

    private ProcessingEnvironment processingEnv;

    public void process(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        this.processingEnv = processingEnv;

        // Process annotations.
        for (CdkAnnotationProcessor processor : processors) {
            processAnnotation(processor, roundEnv);
        }
    }

    protected void processAnnotation(CdkAnnotationProcessor processor, RoundEnvironment environment) {
        Class<? extends Annotation> processedAnnotation = processor.getProcessedAnnotation();
        log.debug("Process all elements annotated with " + processedAnnotation.getName());
        Target target = processedAnnotation.getAnnotation(Target.class);
        Set<? extends Element> rootElements = environment.getRootElements();
        for (Element element : rootElements) {

            if (!sourceCache.isChanged(element)) {
                continue;
            }

            if (isAppropriateTarget(element, target)) {
                processElement(processor, processedAnnotation, element);
            } else {
                for (Element enclosedElement : element.getEnclosedElements()) {
                    if (!sourceCache.isChanged(enclosedElement)) {
                        continue;
                    }
                    if (isAppropriateTarget(enclosedElement, target)) {
                        processElement(processor, processedAnnotation, enclosedElement);
                    }
                }
            }
        }
    }

    private void processElement(CdkAnnotationProcessor processor, Class<? extends Annotation> processedAnnotation,
            Element element) {
        if (null != element.getAnnotation(processedAnnotation)) {
            try {
                log.debug("Process " + element.getSimpleName() + " annotated with " + processedAnnotation.getName());
                processor.process(element, library);
            } catch (CdkProcessingException e) {
                sendError(element, e);
            }
        }
    }

    private boolean isAppropriateTarget(Element element, Target target) {
        boolean match = false;
        ElementKind kind = element.getKind();
        if (null != target) {
            for (ElementType targetType : target.value()) {
                switch (targetType) {
                    case TYPE:
                        match |= ElementKind.CLASS.equals(kind) || ElementKind.INTERFACE.equals(kind)
                                || ElementKind.ENUM.equals(kind);
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
            match = ElementKind.CLASS.equals(kind) || ElementKind.INTERFACE.equals(kind) || ElementKind.ENUM.equals(kind)
                    || ElementKind.PACKAGE.equals(kind) || ElementKind.METHOD.equals(kind) || ElementKind.FIELD.equals(kind);
        }
        return match;
    }

    private void sendError(Element componentElement, Exception e) {
        // rise error and continue.
        processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, e.getMessage(), componentElement);
    }
}
