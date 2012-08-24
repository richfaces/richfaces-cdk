package org.richfaces.cdk.apt;

import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ComponentLibraryProvider implements Provider<ComponentLibrary> {
    
    @Inject
    ComponentLibraryHolder holder;
    
    @Override
    public ComponentLibrary get() {
        return holder.getLibrary();
    }
}
