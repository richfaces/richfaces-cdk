package org.richfaces.cdk.model;

/**
 * <p class="changed_added_4_0">
 * Interface for model components that have related modification time.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public interface Trackable {
    /**
     * <p class="changed_added_4_0">
     * Last modification time for model information.
     * </p>
     *
     * @return
     */
    long lastModified();
}
