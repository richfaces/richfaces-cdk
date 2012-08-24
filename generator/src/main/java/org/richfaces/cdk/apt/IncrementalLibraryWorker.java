package org.richfaces.cdk.apt;

import java.io.File;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class IncrementalLibraryWorker implements LibraryWorker {

    private LibraryWorker delegate;
    
    // TODO use Guice here
    public static LibraryCache javaCache = new LibraryCache(new File("target/java-cache.ser"));
    public static LibraryCache nonJavaCache = new LibraryCache(new File("target/nonjava-cache.ser"));
    
    @Inject
    ComponentLibraryHolder holder;
    
    private ComponentLibrary javaSourcesLibrary = new ComponentLibrary();
    private ComponentLibrary nonJavaSourcesLibrary = new ComponentLibrary();
    private ComponentLibrary composedLibrary = new ComponentLibrary();
    
    @Inject
    public IncrementalLibraryWorker(Injector injector) {
        delegate = new LibraryWorkerImpl();
        injector.injectMembers(delegate);
    }

    @Override
    public void beforeJavaSourceProcessing() {
        delegate.beforeJavaSourceProcessing();
        
        holder.setLibrary(javaSourcesLibrary);
    }

    @Override
    public void processJavaSource(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        if (!javaCache.available()) {
            delegate.processJavaSource(processingEnv, roundEnv);
        }
    }

    @Override
    public void afterJavaSourceProcessing() {
        delegate.afterJavaSourceProcessing();
        
        if (javaCache.available()) {
            javaSourcesLibrary = javaCache.load();
        } else {
            javaCache.save(javaSourcesLibrary);
        }
    }

    @Override
    public void processNonJavaSources() throws CdkException {
        ComponentLibrary cachedLibrary = new ComponentLibrary();
        ComponentLibrary additionsToLibrary = new ComponentLibrary();
        
        if (nonJavaCache.available()) {
            cachedLibrary = nonJavaCache.load();
        }
        
        holder.setLibrary(additionsToLibrary);
        delegate.processNonJavaSources();
        
        nonJavaSourcesLibrary.merge(cachedLibrary);
        nonJavaSourcesLibrary.markUnchanged();
        nonJavaSourcesLibrary.merge(additionsToLibrary);
        
        nonJavaCache.save(nonJavaSourcesLibrary);
    }

    @Override
    public void verify() throws CdkException {
        composedLibrary.merge(javaSourcesLibrary);
        composedLibrary.merge(nonJavaSourcesLibrary);
        
        holder.setLibrary(composedLibrary);
        
        delegate.verify();
    }

    @Override
    public void generate() throws CdkException {
        delegate.generate();
    }

}
