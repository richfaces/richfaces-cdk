package org.richfaces.cdk.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class IncrementalLibraryWorker implements LibraryWorker {

    private LibraryWorker delegate;

    @Inject
    LibraryCache cache;
    
    @Inject
    ComponentLibraryHolder holder;
    
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
        if (!cache.available()) {
            delegate.processJavaSource(processingEnv, roundEnv);
        }
    }

    @Override
    public void afterJavaSourceProcessing() {
        delegate.afterJavaSourceProcessing();
    }

    @Override
    public void processNonJavaSources() throws CdkException {
        if (!cache.available()) {
            delegate.processNonJavaSources();
        }
    }

    @Override
    public void verify() throws CdkException {
        if (!cache.available()) {
            ComponentLibrary processed = holder.getLibrary();
            cache.save(processed);
        } else {
            ComponentLibrary loaded = cache.load();
            holder.setLibrary(loaded);
        }
        
        delegate.verify();
    }

    @Override
    public void generate() throws CdkException {
        delegate.generate();
    }

}
