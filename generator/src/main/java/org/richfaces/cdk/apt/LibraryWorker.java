package org.richfaces.cdk.apt;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.ModelBuilder;
import org.richfaces.cdk.ModelValidator;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;

public class LibraryWorker {

    @Inject
    private Logger log;
    
    @Inject
    JavaSourceProcessor javaSourceProcessor;
    
    @Inject
    private Set<CdkWriter> generators;

    @Inject
    private ComponentLibrary library;

    @Inject
    private Set<ModelBuilder> builders;
    @Inject
    private ModelValidator validator;

    protected void beforeJavaSourceProcessing() {
    }
    
    protected void processJavaSource(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        javaSourceProcessor.process(processingEnv, roundEnv);
    }
    
    protected void afterJavaSourceProcessing() {
        processNonJavaSources();
        verify();
        if (0 == log.getErrorCount()) {
            generate();
        }
    }
    
    protected void generate() throws CdkException {
        if (0 == log.getErrorCount()) {
            // processing over, generate files.
            for (CdkWriter generator : generators) {
                generator.render(library);
            }
        }
    }

    protected void processNonJavaSources() throws CdkException {
        for (ModelBuilder builder : builders) {
            log.debug("Run builder " + builder.getClass().getName());
            try {
                builder.build();
            } catch (CdkException e) {
                // TODO: sendError(e);
                e.printStackTrace();
            }
        }
    }

    protected void verify() throws CdkException {
        try {
            log.debug("Validate model");
            validator.verify(library);
        } catch (CdkException e) {
            // TODO: sendError(e);
            e.printStackTrace();
        }
    }
}
