package org.richfaces.cdk.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;

public interface LibraryWorker {

    void beforeJavaSourceProcessing();

    void processJavaSource(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv);

    void afterJavaSourceProcessing();

    void generate() throws CdkException;

    void processNonJavaSources() throws CdkException;

    void verify() throws CdkException;

}