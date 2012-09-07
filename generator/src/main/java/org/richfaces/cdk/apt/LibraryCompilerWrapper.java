package org.richfaces.cdk.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;

public class LibraryCompilerWrapper implements LibraryCompiler {

    protected LibraryCompiler delegate;

    public LibraryCompilerWrapper(LibraryCompiler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void beforeJavaSourceProcessing() {
        this.delegate.beforeJavaSourceProcessing();
    }

    @Override
    public void processJavaSource(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        this.delegate.processJavaSource(processingEnv, roundEnv);
    }

    @Override
    public void afterJavaSourceProcessing() {
        this.delegate.afterJavaSourceProcessing();
    }

    @Override
    public void processNonJavaSources() throws CdkException {
        this.delegate.processNonJavaSources();
    }

    @Override
    public void completeLibrary() throws CdkException {
        this.delegate.completeLibrary();
    }
}
