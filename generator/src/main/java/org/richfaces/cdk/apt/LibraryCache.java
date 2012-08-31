package org.richfaces.cdk.apt;

import org.richfaces.cdk.model.ComponentLibrary;

public interface LibraryCache {

    String CACHE_ENABLED_OPTION = "libraryCachingEnabled";

    boolean available();

    boolean storedBefore(long time);

    ComponentLibrary load();

    void save(ComponentLibrary library);
}
