package org.richfaces.cdk.apt;

import static org.richfaces.cdk.apt.CacheType.JAVA_SOURCES;
import static org.richfaces.cdk.apt.CacheType.NON_JAVA_SOURCES;

import org.richfaces.cdk.Cache;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentLibraryHolder;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class IncrementalLibraryCompiler extends LibraryCompilerWrapper {

    @Inject
    @Cache(JAVA_SOURCES)
    public LibraryCache javaCache;

    @Inject
    @Cache(NON_JAVA_SOURCES)
    public LibraryCache nonJavaCache;

    @Inject
    ComponentLibraryHolder holder;

    private ComponentLibrary javaSourcesLibrary = new ComponentLibrary();
    private ComponentLibrary nonJavaSourcesLibrary = new ComponentLibrary();

    @Inject
    public IncrementalLibraryCompiler(Injector injector) {
        super(new DefaultLibraryCompiler());
        injector.injectMembers(delegate);
    }

    @Override
    public void beforeJavaSourceProcessing() {
        super.beforeJavaSourceProcessing();

        holder.setLibrary(new ComponentLibrary());
    }

    @Override
    public void afterJavaSourceProcessing() {
        super.afterJavaSourceProcessing();
        ComponentLibrary additionsToLibrary = holder.getLibrary();

        if (javaCache.available()) {
            ComponentLibrary cachedLibrary = javaCache.load();
            javaSourcesLibrary.merge(cachedLibrary);
            javaSourcesLibrary.markUnchanged();
        }

        javaSourcesLibrary.merge(additionsToLibrary);

        javaCache.save(javaSourcesLibrary);

        javaSourcesLibrary.stopTrackingChanges();
    }

    @Override
    public void processNonJavaSources() throws CdkException {

        if (nonJavaCache.available()) {
            ComponentLibrary cachedLibrary = nonJavaCache.load();
            nonJavaSourcesLibrary.merge(cachedLibrary);
            nonJavaSourcesLibrary.markUnchanged();
        }

        ComponentLibrary additionsToLibrary = new ComponentLibrary();
        holder.setLibrary(additionsToLibrary);
        super.processNonJavaSources();
        nonJavaSourcesLibrary.merge(additionsToLibrary);

        nonJavaCache.save(nonJavaSourcesLibrary);

        nonJavaSourcesLibrary.stopTrackingChanges();
    }

    @Override
    public void completeLibrary() throws CdkException {
        ComponentLibrary composedLibrary = new ComponentLibrary();
        composedLibrary.stopTrackingChanges();

        composedLibrary.merge(javaSourcesLibrary);
        composedLibrary.merge(nonJavaSourcesLibrary);

        holder.setLibrary(composedLibrary);
    }
}
