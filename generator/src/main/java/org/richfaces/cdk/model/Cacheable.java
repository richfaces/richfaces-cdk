package org.richfaces.cdk.model;

/**
 * Marks given model which can be cached between compiler runs
 */
public interface Cacheable {

    /**
     * Marks the model unchanged, so further changes will make it changed.
     *
     * You may do any changes which should not be tracked and then call this method to track further changes.
     */
    void markUnchanged();

    /**
     * Says whenever this model has been changed after last call to {@link #markUnchanged()}.
     */
    boolean hasChanged();

    /**
     * Stops tracking any changes, making the model available for merging without tracking changes.
     */
    void stopTrackingChanges();
}
