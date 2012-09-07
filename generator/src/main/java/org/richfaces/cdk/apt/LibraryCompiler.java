package org.richfaces.cdk.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;

public interface LibraryCompiler {

    void beforeJavaSourceProcessing();

    void processJavaSource(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv);

    void afterJavaSourceProcessing();

    void processNonJavaSources() throws CdkException;

    void completeLibrary() throws CdkException;
}