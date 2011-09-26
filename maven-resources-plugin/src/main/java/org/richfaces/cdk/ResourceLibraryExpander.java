package org.richfaces.cdk;

import java.util.Collection;

import org.richfaces.application.ServiceTracker;
import org.richfaces.renderkit.html.ResourceLibraryRenderer;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;
import org.richfaces.resource.ResourceLibraryFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ResourceLibraryExpander {

    public Collection<ResourceKey> expandResourceLibraries(Collection<ResourceKey> resources) {
        ResourceLibraryFactory factory = ServiceTracker.getService(ResourceLibraryFactory.class);
        Collection<ResourceKey> expandedResources = Sets.newLinkedHashSet();

        for (ResourceKey resourceKey : resources) {
            if (resourceKey.getResourceName().endsWith(ResourceLibraryRenderer.RESOURCE_LIBRARY_EXTENSION)) {

                String libraryName = resourceKey.getLibraryName();
                String resourceName = resourceKey.getResourceName().substring(0,
                        resourceKey.getResourceName().length() - ResourceLibraryRenderer.RESOURCE_LIBRARY_EXTENSION.length());
                ResourceLibrary resourceLibrary = factory.getResourceLibrary(resourceName, libraryName);

                if (resourceLibrary == null) {
                    throw new IllegalArgumentException("Resource library is null: " + libraryName + ":" + resourceName);
                }

                for (ResourceKey expandedKey : resourceLibrary.getResources()) {
                    expandedResources.add(expandedKey);
                }

            } else {
                expandedResources.add(resourceKey);
            }
        }

        return expandedResources;
    }
}
