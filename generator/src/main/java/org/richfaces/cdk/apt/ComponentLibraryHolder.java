package org.richfaces.cdk.apt;

import org.richfaces.cdk.model.ComponentLibrary;

public class ComponentLibraryHolder {

    private ComponentLibrary library = new ComponentLibrary();

    public ComponentLibrary getLibrary() {
        return library;
    }

    public void setLibrary(ComponentLibrary library) {
        this.library = library;
    }
}
