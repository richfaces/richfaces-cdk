package org.richfaces.cdk.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class IncrementalLibraryWorker implements LibraryWorker {
    
    private LibraryWorker delegate;

    @Inject
    public IncrementalLibraryWorker(Injector injector) {
        delegate = new LibraryWorkerImpl();
        injector.injectMembers(delegate);
    }
    
    @Override
    public void beforeJavaSourceProcessing() {
        delegate.beforeJavaSourceProcessing();
    }

    @Override
    public void processJavaSource(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        delegate.processJavaSource(processingEnv, roundEnv);
    }

    @Override
    public void afterJavaSourceProcessing() {
        delegate.afterJavaSourceProcessing();
    }

    @Override
    public void generate() throws CdkException {
        delegate.generate();
    }

    @Override
    public void processNonJavaSources() throws CdkException {
        delegate.processNonJavaSources();
    }

    @Override
    public void verify() throws CdkException {
        delegate.verify();
    } 

}
