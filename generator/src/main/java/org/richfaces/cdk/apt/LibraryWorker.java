package org.richfaces.cdk.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;

public interface LibraryWorker {

    public abstract void beforeJavaSourceProcessing();

    public abstract void processJavaSource(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv);

    public abstract void afterJavaSourceProcessing();

    public abstract void generate() throws CdkException;

    public abstract void processNonJavaSources() throws CdkException;

    public abstract void verify() throws CdkException;

}