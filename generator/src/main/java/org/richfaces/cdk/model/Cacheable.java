package org.richfaces.cdk.model;

public interface Cacheable {
    
    void markUnchanged();
    
    boolean hasChanged();
}
