package org.richfaces.cdk.model;

/**
 * <p class="changed_added_4_0">
 * Defines element for view elements, for which tags should be generated.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface ViewElement {
    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the tags
     */
    ModelCollection<TagModel> getTags();
}